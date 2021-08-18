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

import kltn.util.Common;
import kltn.util.Constants;

@Component
public class FirebaseShop {

	private FirebaseApp onceApp;

	@Autowired
	private Constants contants;

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

		Query query = ref.child(String.valueOf(o.getUserId()));
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
					userId.put(String.valueOf(o.getUserId()), orderId);
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
			query = ref.child(String.valueOf(r.getUserId()));
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
						userId.put(String.valueOf(r.getUserId()), productId);
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
					userId.put(String.valueOf(r.getUserId()), productId);
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
		query = ref.child(String.valueOf(r.getUserId()));
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
					userId.put(String.valueOf(r.getUserId()), productId);
					System.out.println(userId);
					ref.updateChildrenAsync(userId);
					return;
				}
				childs.put("childs", "");
				childs.put("content", r.getContent());
				childs.put("createDate", r.getCreateDate());
				commentId.put(String.valueOf(r.getCommentId()), childs);
				productId.put(String.valueOf(r.getProductId()), commentId);
				userId.put(String.valueOf(r.getUserId()), productId);
				ref.updateChildrenAsync(userId);
				return;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
	}
}
