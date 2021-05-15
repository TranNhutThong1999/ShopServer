package kltn.service;

import kltn.dto.ShopDTO;

public interface IShopService {
	ShopDTO Save(ShopDTO s);
	ShopDTO findOneById(int id) throws Exception;
	ShopDTO findOneByUserName(String userName) ;
	void delete(String userName);
}
