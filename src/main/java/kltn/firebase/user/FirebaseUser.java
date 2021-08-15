package kltn.firebase.user;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

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

@Component
public class FirebaseUser {

//	@Value("${firebase.realtime.user.path}")
//	private String fileUrl;
	private FirebaseApp secondaryApp = null;
	
	@PostConstruct
	public void initialize() {
		try {
			File f = new ClassPathResource("FirebaseUser.json").getFile();
			if (f.exists())
				System.out.println("ok");
			FileInputStream serviceAccount = new FileInputStream(f);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://hi-shop-f90c9-default-rtdb.firebaseio.com").build();
			
			 secondaryApp = FirebaseApp.initializeApp(options, "secondary");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void insertOrderStatus(UpdateStatusOrderUser o) {
//		FirebaseApp app = FirebaseApp.getInstance("user");
//		FirebaseDatabase database = FirebaseDatabase.getInstance(app);
//		DatabaseReference ref = database.getReference("orders");
//
//		Map<String, UpdateStatusOrderUser> users = new HashMap<>();
//		String id = UUID.randomUUID().toString();
//		o.setId(id);
//		users.put(id, o);
//		ref.setValueAsync(users);
//	}	

	public void updateOrderStatus(UpdateStatusOrderUser o) {
		FirebaseDatabase database = FirebaseDatabase.getInstance(secondaryApp);
		DatabaseReference ref = database.getReference("orders");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				UpdateStatusOrderUser realtime = null;
				for (DataSnapshot dsp : snapshot.getChildren()) {
					realtime = new UpdateStatusOrderUser();
					realtime.setId(dsp.child("id").getValue().toString());
					realtime.setCode(dsp.child("code").getValue().toString());
					realtime.setStatus(o.getStatus());
					realtime.setUserId(Integer.valueOf(dsp.child("userId").getValue().toString()));
					realtime.setMessage(o.getMessage());
					if (o.getUserId() == realtime.getUserId() && o.getCode().equals(realtime.getCode())) {
						Map<String, Object> users = new HashMap<>();
						users.put(realtime.getId(), realtime);
						ref.updateChildrenAsync(users);
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
	}
}
