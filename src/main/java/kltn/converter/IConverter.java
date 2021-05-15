package kltn.converter;

public interface  IConverter<S,D> {
	 S toEntity(D d) throws Exception;
	 D toDTO(S s);
}
