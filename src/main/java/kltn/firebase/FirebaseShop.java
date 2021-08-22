package kltn.firebase;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import kltn.entity.DeviceToken;
import kltn.repository.DeviceTokenRepository;
import kltn.repository.NotificationRepository;
import kltn.util.Common;
import kltn.util.Constants;

@Component
public class FirebaseShop {

	private FirebaseApp onceApp;

	@Autowired
	private Constants contants;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private DeviceTokenRepository deviceTokenRepository; 
	
	@PostConstruct
	public void initialize() {
		try {
			File f = new ClassPathResource(contants.getFileShop()).getFile();
			FileInputStream serviceAccount = new FileInputStream(f);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://hi-shop-sl-default-rtdb.firebaseio.com").build();
			onceApp = FirebaseApp.initializeApp(options, "once");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateOrderStatus(UpdateStatusOrder o) {
		FirebaseDatabase database = FirebaseDatabase.getInstance(onceApp);
		DatabaseReference ref = database.getReference("orders");
		Map<String, Object> item = new HashMap<>();
		Map<String, Object> orderId = new HashMap<>();
		Map<String, Object> userId = new HashMap<>();
		System.out.println("vào shop");
		Query query = ref.child(o.getUserId());
		query.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				if (snapshot.exists()) {
					for (DataSnapshot data : snapshot.getChildren()) {
						orderId.put(String.valueOf(data.getKey()), data.getValue());
					}
					item.put("code", o.getCode());
					item.put("createDate", o.getCreateDate());
					item.put("shopId", o.getShopId());
					item.put("status", o.getStatus());
					orderId.put(String.valueOf(o.getOrderId()), item);
					userId.put(o.getUserId(), orderId);
					System.out.println("run shop "+ o.getUserId());
					ref.updateChildrenAsync(userId);
					return;
				}
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void updateRealtimeCommentShop(RealtimeComment r) {
		FirebaseDatabase database = FirebaseDatabase.getInstance(onceApp);
		DatabaseReference ref = database.getReference("comments");
		Map<String, Object> userId = new HashMap<>();
		Map<String, Object> productId = new HashMap<>();
		Map<String, Object> commentId = new HashMap<>();
		Map<String, Object> childs = new HashMap<>();
		Map<String, Object> childCommentId = new HashMap<>();
		Map<String, Object> item = new HashMap<>();

		Query query = null;
		if (r.getParentCommentId() != null) {
			query = ref.child(r.getShopId());
			query.addListenerForSingleValueEvent(new ValueEventListener() {

				@Override
				public void onDataChange(DataSnapshot snapshot) {
					// TODO Auto-generated method stub
					if (snapshot.exists()) {

						for (DataSnapshot data : snapshot.child(String.valueOf(r.getProductId()))
								.child(String.valueOf(r.getParentCommentId())).child("childs").getChildren()) {
							childCommentId.put(String.valueOf(data.getKey()), data.getValue());
						}

						for (DataSnapshot data : snapshot.child(String.valueOf(r.getProductId()))
								.child(String.valueOf(r.getParentCommentId())).getChildren()) {
							childs.put(String.valueOf(data.getKey()), data.getValue());// childs, content, date
						}

						for (DataSnapshot data : snapshot.child(String.valueOf(r.getProductId())).getChildren()) {
							commentId.put(String.valueOf(data.getKey()), data.getValue());// list commentId
						}

						for (DataSnapshot data : snapshot.getChildren()) {
							productId.put(String.valueOf(data.getKey()), data.getValue());
						}
						item.put("content", r.getContent());
						item.put("createDate", r.getCreateDate());
						childCommentId.put(String.valueOf(r.getCommentId()), item);
						childs.put("childs", childCommentId);
						commentId.put(String.valueOf(r.getParentCommentId()), childs);
						productId.put(String.valueOf(r.getProductId()), commentId);
						userId.put(r.getShopId(), productId);
						System.out.println(userId);
						ref.updateChildrenAsync(userId);
						return;
					}
					item.put("childs", "");
					item.put("content", r.getContent());
					item.put("createDate", r.getCreateDate());
					childCommentId.put(String.valueOf(r.getCommentId()), item);
					childs.put("childs", childCommentId);

					commentId.put(String.valueOf(r.getParentCommentId()), childs);
					productId.put(String.valueOf(r.getProductId()), commentId);
					userId.put(r.getShopId(), productId);
					ref.updateChildrenAsync(userId);
					return;
				}

				@Override
				public void onCancelled(DatabaseError error) {
					// TODO Auto-generated method stub

				}

			});
			return;
		}
		query = ref.child(r.getShopId());
		query.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.exists()) {
					for (DataSnapshot data : snapshot.child(String.valueOf(r.getProductId())).getChildren()) {
						commentId.put(String.valueOf(data.getKey()), data.getValue());
					}

					for (DataSnapshot data : snapshot.getChildren()) {
						productId.put(String.valueOf(data.getKey()), data.getValue());
					}

					item.put("childs", "");
					item.put("content", r.getContent());
					item.put("createDate", r.getCreateDate());
					commentId.put(String.valueOf(r.getCommentId()), item);
					productId.put(String.valueOf(r.getProductId()), commentId);
					userId.put(r.getShopId(), productId);
					System.out.println(userId);
					ref.updateChildrenAsync(userId);
					return;
				}
				childs.put("childs", "");
				childs.put("content", r.getContent());
				childs.put("createDate", r.getCreateDate());
				commentId.put(String.valueOf(r.getCommentId()), childs);
				productId.put(String.valueOf(r.getProductId()), commentId);
				userId.put(r.getShopId(), productId);
				ref.updateChildrenAsync(userId);
				return;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
	}
	public void updateNotificationOrder(kltn.entity.Notification noti) {
		noti = Common.setDataOrder(noti);
		NotiOrder data = new NotiOrder(notificationRepository.save(noti));
		FirebaseMessaging f = FirebaseMessaging.getInstance(onceApp);
		DeviceToken de = deviceTokenRepository.findOneByShop_Id(noti.getShop().getId()).get();
		Notification notification = Notification.builder().setTitle(noti.getTitle()).setBody(noti.getSubTitle())
				.build();
		ObjectMapper o = new ObjectMapper();
		Message message = null;
		try {
			message = Message.builder().putData("data", o.writeValueAsString(data)).setToken(de.getFCMToken())
					.setNotification(notification).build();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String response = null;
		try {
			response = f.send(message);
			System.out.println(response);
			
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}

	public void updateNotificationComment(kltn.entity.Notification noti) {
		FirebaseMessaging f = FirebaseMessaging.getInstance(onceApp);
		NotiComment data = new NotiComment(notificationRepository.save(noti));
		DeviceToken de = deviceTokenRepository.findOneByShop_Id(noti.getShop().getId()).get();
		Notification notification = Notification.builder().setTitle(noti.getTitle()).setBody(noti.getSubTitle())
				.build();
		ObjectMapper o = new ObjectMapper();
		Message message = null;
		try {
			message = Message.builder().putData("data", o.writeValueAsString(data)).setToken(de.getFCMToken())
					.setNotification(notification).build();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String response = null;
		try {
			response = f.send(message);
			System.out.println(response);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
}
