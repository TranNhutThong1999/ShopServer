package kltn.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.output.CommentOuput;
import kltn.converter.CommentConverter;
import kltn.converter.PhotoConverter;
import kltn.entity.Comment;
import kltn.entity.Notification;
import kltn.entity.Product;
import kltn.entity.Reply;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.event.PushEventCommentShop;
import kltn.event.PushEventCommentUser;
import kltn.event.PushEventNotiCommentShop;
import kltn.event.PushEventNotiCommentUser;
import kltn.firebase.RealtimeComment;
import kltn.repository.CommentRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ReplyRepository;
import kltn.repository.ShopRepository;
import kltn.service.ICommentService;
import kltn.util.Common;

@Service
public class CommentService implements ICommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CommentConverter commentConverter;

	@Autowired
	private ShopRepository ShopRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper modelMap;

	@Autowired
	private ReplyRepository replyRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public CommentOuput save(CommentOuput m, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Optional<Product> pro = productRepository.findOneByIdAndShop_Id(m.getProductId(), Common.getIdFromAuth(auth));
		if (pro.isPresent()) {
			Shop u = ShopRepository.findOneById(Common.getIdFromAuth(auth)).get();
			Comment cm = null;
			if (m.getParentId() == null) {
				cm = commentConverter.toEntity(m);
				cm.setShop(u);
				cm.setProduct(productRepository.findOneById(m.getProductId())
						.orElseThrow(() -> new Exception("productId was not found")));
				cm = commentRepository.save(cm);
				return commentConverter.toDTO(cm);
			} else {
				cm = commentRepository.findById(m.getParentId())
						.orElseThrow(() -> new Exception("commentId was not found"));
				Reply rl = modelMap.map(m, Reply.class);
				rl.setComment(cm);
				rl.setShop(u);
				Reply reply = replyRepository.save(rl);
				// notify user
				if (cm.getUser() != null && cm.getShop() == null) {
					RealtimeComment dataUser = new RealtimeComment(reply.getId(), cm.getProduct().getId(),
							cm.getUser().getId(), cm.getId(), reply.getContent(), reply.getCreatedDate());
					System.out.println("push realtime user parent is User " + cm.getUser().getId());
					applicationEventPublisher.publishEvent(new PushEventCommentUser(this, dataUser));

					// notify to user parent
					Notification notificationUser = new Notification();
					notificationUser.setAvatar(reply.getShop().getAvatar());
					notificationUser.setType(Common.NOTI_CMT);
					notificationUser.setTitle(reply.getShop().getName());
					notificationUser.setSubTitle("Đã trả lời bình luận của bạn trong một sản phẩm");
					notificationUser.setReply(reply);
					notificationUser.setTime(Common.parse(reply.getCreatedDate()));
					notificationUser.setUser(cm.getUser());
					System.out.println("push noti user parent is User" + cm.getUser().getId());
					applicationEventPublisher.publishEvent(new PushEventNotiCommentUser(this, notificationUser));
				}
				// notification to user comment childs
				Set<Integer> done = new HashSet();
				for (Reply o : cm.getReplies()) {
					if (!done.contains(o.getId())) { // check user was notified
						if (o.getUser() != null && cm.getUser() != null
								&& o.getUser().getId().equals(cm.getUser().getId()))
							continue;
						if (o.getShop() != null && cm.getShop() != null
								&& o.getShop().getId().equals(cm.getShop().getId()))
							continue;
						if (o.getShop() != null && o.getUser() == null
								&& o.getShop().getId().equals(cm.getProduct().getShop().getId()))
							continue;

						done.add(o.getId());
						Notification child = new Notification();
						child.setAvatar(reply.getShop().getAvatar());
						child.setType(Common.NOTI_CMT);
						child.setTitle(reply.getShop().getName());
						child.setSubTitle("Đã trả lời bình luận một sản phẩm");
						child.setReply(reply);
						child.setTime(Common.parse(reply.getCreatedDate()));
						if (o.getUser() == null && o.getShop() != null) {// check user or shop
							System.out.println("push noti Shop child comment " + o.getShop().getId());
							child.setShop(o.getShop());
							applicationEventPublisher.publishEvent(new PushEventNotiCommentShop(this, child));

							RealtimeComment dataShop = new RealtimeComment(reply.getId(), cm.getProduct().getId(),
									o.getShop().getId(), cm.getId(), reply.getContent(), reply.getCreatedDate());
							System.out.println("push realtime shop child comment " + o.getShop().getId());
							applicationEventPublisher.publishEvent(new PushEventCommentShop(this, dataShop));
							continue;
						}
						RealtimeComment dataUser = new RealtimeComment(reply.getId(), cm.getProduct().getId(),
								o.getUser().getId(), cm.getId(), reply.getContent(), reply.getCreatedDate());
						System.out.println("push realtime user child comment " + o.getUser().getId());
						applicationEventPublisher.publishEvent(new PushEventCommentUser(this, dataUser));

						System.out.println("push noti user child comment " + o.getUser().getId());
						child.setUser(o.getUser());
						applicationEventPublisher.publishEvent(new PushEventNotiCommentUser(this, child));
					}

				}
				return commentConverter.toReply(reply);
			}
		}
		throw new Exception("auth");
	}

	@Override
	public void delete(Authentication auth, int id) {
		// TODO Auto-generated method stub
		Optional<Comment> comment = commentRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth));
		if (comment.isPresent()) {
			commentRepository.delete(comment.get());
		}
	}

	@Override
	public List<CommentOuput> findByShopAndLimit(Authentication auth, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommentOuput> getListCommentProduct(int productId, Authentication auth) {
		// TODO Auto-generated method stub
		Sort sort =Sort.by(Sort.Direction.DESC, "createdDate");
		return commentRepository.findAllByProduct_IdAndProduct_Shop_Id(productId, Common.getIdFromAuth(auth), sort).stream()
				.map(commentConverter::toDTO).collect(Collectors.toList());
	}

//	@Override
//	public List<CommentOuput> findByShopAndLimit(Authentication auth, int limit) {
//		// TODO Auto-generated method stub
//		commentRepository.find
//		return null;
//	}
}
