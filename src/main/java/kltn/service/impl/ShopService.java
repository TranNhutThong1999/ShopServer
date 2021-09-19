package kltn.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.threeten.bp.Year;

import kltn.SHOPConstant;
import kltn.api.input.ShopDetail;
import kltn.converter.AddressConverter;
import kltn.converter.PhotoConverter;
import kltn.converter.ShopConverter;
import kltn.dto.AddressDTO;
import kltn.dto.NotificationDTO;
import kltn.dto.ShopDTO;
import kltn.dto.StatisticalDTO;
import kltn.entity.Address;
import kltn.entity.DeviceToken;
import kltn.entity.District;
import kltn.entity.Notification;
import kltn.entity.Order;
import kltn.entity.Province;
import kltn.entity.Shop;
import kltn.entity.Wards;
import kltn.repository.AddressRepository;
import kltn.repository.DeviceTokenRepository;
import kltn.repository.DistrictRepository;
import kltn.repository.NotificationRepository;
import kltn.repository.OrderRepository;
import kltn.repository.ProvincialRepository;
import kltn.repository.ShopRepository;
import kltn.repository.WardsRepository;
import kltn.service.IShopService;
import kltn.util.Common;
import kltn.util.Constants;
import kltn.util.EmailService;
import kltn.util.StatisticalMonth;

@Service
public class ShopService implements IShopService {
	Logger logger = LoggerFactory.getLogger(ShopService.class);

	@Autowired
	private ShopConverter shopConverter;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProvincialRepository provincialRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardsRepository wardsRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AddressConverter addressConverter;

	@Autowired
	private DeviceTokenRepository deviceTokenRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private Constants constants;

	@Autowired
	private PhotoConverter photoConverter;

	@Override
	public ShopDTO save(ShopDTO s) {
		// TODO Auto-generated method stub
		Shop e = new Shop();
		e.setPhone(s.getPhone());
		e.setEmail(s.getEmail());
		e.setPassword(encoder.encode(s.getPassword()));
		e.setEnable(false);
		e.generateToken();
		e.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
		emailService.sendRegisterMessage(e.getEmail(), e.getOtp());
//		emailService.sendSimpleMessage(e.getEmail(), "HI SHOP", "Your code: " + e.getOtp());
		return shopConverter.toDTO(shopRepository.save(e));
	}

	@Override
	public ShopDTO createDetail(ShopDetail s) throws Exception {
		Shop shop = shopRepository.findOneById(s.getId()).orElseThrow(() -> {
			logger.error("shopId was not found " + s.getId());
			return new Exception("shopId was not found " + s.getId());
		});
		if (shop.getCode() != null) {
			logger.error("code existed");
			throw new Exception("code existed " + s.getId());
		}
		shop.setName(s.getName());
		shop.setCode(UUID.randomUUID().toString());
		shop.setHotLine(s.getHotLine());
		shop.setWebsite(s.getWebsite());
		Address address = new Address();
		address.setLocation(s.getLocation());

		Optional<Province> p = provincialRepository.findOneByCode(s.getProvince().getCode());
		if (!p.isPresent()) {
			Province pro = modelMapper.map(s.getProvince(), Province.class);
			provincialRepository.save(pro);
			address.setProvince(pro);
		} else
			address.setProvince(p.get());

		Optional<District> d = districtRepository.findOneByCode(s.getDistrict().getCode());
		if (!d.isPresent()) {
			District dis = modelMapper.map(s.getDistrict(), District.class);
			districtRepository.save(dis);
			address.setDistrict(dis);
		} else
			address.setDistrict(d.get());

		Optional<Wards> w = wardsRepository.findOneByCode(s.getWards().getCode());
		if (!w.isPresent()) {
			Wards wards = modelMapper.map(s.getWards(), Wards.class);
			wardsRepository.save(wards);
			address.setWards(wards);
		} else
			address.setWards(w.get());
		shop.setAddress(address);
		Shop sh = shopRepository.save(shop);
		addressRepository.save(address);
		return shopConverter.toDTO(sh);
	}

	@Override
	public ShopDTO getOne(Authentication auth) {
		// TODO Auto-generated method stub
		return shopConverter.toDTO(shopRepository.findById(Common.getIdFromAuth(auth)).get());
	}

	@Override
	public void delete(Authentication auth) {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		shopRepository.delete(shop);
	}

	@Override
	public String sendOTP(String mail) throws Exception {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findOneByEmail(mail).orElseThrow(() -> new Exception("Email was not found"));
		shop.generateToken();
		shop.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
		shop = shopRepository.save(shop);
		emailService.sendOtpMessage(mail, shop.getOtp());
		return shop.getId();
	}

	@Override
	public String checkOTP(String otp, String shopId) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("otp was not found"));
		if (s.isAfterTime())
			throw new Exception("Opt expired");
		shopRepository.save(s);
		return s.getId();
	}

	@Override
	public void verify(String otp, String shopId) throws Exception {
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("otp was not found"));
		s.setEnable(true);
		s.setOtp("");
		shopRepository.save(s);
	}

	@Override
	public void changePassword(String password, Authentication auth) {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		s.setPassword(encoder.encode(password));
		shopRepository.save(s);
	}

	@Override
	public void updatePassword(String shopId, String password, String otp) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("shopId was not found"));
		s.setPassword(encoder.encode(password));
		s.setOtp("");
		shopRepository.save(s);
	}

	@Override
	public AddressDTO getAddress(Authentication auth) {
		// TODO Auto-generated method stub
		Address ad = shopRepository.findOneById(Common.getIdFromAuth(auth)).get().getAddress();
		return addressConverter.toDTO(ad);
	}

	@Override
	public ShopDTO update(ShopDTO shop, Authentication auth) {
		// TODO Auto-generated method stub
		Shop old = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		// Shop s = shopConverter.toEntity(shop);
		old.setName(shop.getName());
		old.setPhone(shop.getPhone());
		old.setHotLine(shop.getHotLine());
		old.setWebsite(shop.getWebsite());
		if (!shop.getEmail().equals(old.getEmail())) {
			old.setEmail(shop.getEmail());
			old.setEnable(false);
			old.generateToken();
			old.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
			emailService.sendRegisterMessage(old.getEmail(), old.getOtp());
		}
		Address address = old.getAddress();
		address.setLocation(shop.getLocation());

		Optional<Province> p = provincialRepository.findOneByCode(shop.getProvince().getCode());
		if (!p.isPresent()) {
			Province pro = modelMapper.map(shop.getProvince(), Province.class);
			pro = provincialRepository.save(pro);
			address.setProvince(pro);
		} else
			address.setProvince(p.get());

		Optional<District> d = districtRepository.findOneByCode(shop.getDistrict().getCode());
		if (!d.isPresent()) {
			District dis = modelMapper.map(shop.getDistrict(), District.class);
			dis = districtRepository.save(dis);
			address.setDistrict(dis);
		} else
			address.setDistrict(d.get());

		Optional<Wards> w = wardsRepository.findOneByCode(shop.getWards().getCode());
		if (!w.isPresent()) {
			Wards wards = modelMapper.map(shop.getWards(), Wards.class);
			wards = wardsRepository.save(wards);
			address.setWards(wards);
		} else
			address.setWards(w.get());

		old.setAddress(address);

		return shopConverter.toDTO(shopRepository.save(old));
	}

	@Override
	public void saveFCMToken(String token, Authentication auth) {
		// TODO Auto-generated method stub
		Optional<DeviceToken> de = deviceTokenRepository.findOneByShop_Id(Common.getIdFromAuth(auth));
		DeviceToken d = null;
		if (!de.isPresent()) {
			d = new DeviceToken();
			d.setFCMToken(token);
			d.setShop(shopRepository.findById(Common.getIdFromAuth(auth)).get());
			d.setCreateDate(new Date());
			deviceTokenRepository.save(d);
		} else {
			d = de.get();
			d.setFCMToken(token);
			d.setCreateDate(new Date());
			deviceTokenRepository.save(d);
		}

	}

	@Override
	public Page<NotificationDTO> getListNoti(Authentication auth, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return notificationRepository.findByShop_Id(Common.getIdFromAuth(auth), pageable).map((x) -> {
			NotificationDTO noti = modelMapper.map(x, NotificationDTO.class);
			if (x.getOrder() == null) {
				if (x.getComment() != null) {
					noti.setCommentId(x.getComment().getId());
					noti.setProductId(x.getComment().getProduct().getId());
				} else {
					noti.setCommentId(x.getReply().getId());
					noti.setProductId(x.getReply().getId());
				}

				return noti;
			} else {
				noti.setOrderId(x.getOrder().getId());
				noti.setOrderCode(x.getOrder().getOrderCode());
			}
			noti.setAvatar(photoConverter.tolinkAvNoti(noti.getAvatar()));
			return noti;
		});
	}

	@Override
	public void setIsSeen(Authentication auth, String id) {
		// TODO Auto-generated method stub
		Optional<Notification> no = notificationRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth));
		if (no.isPresent()) {
			Notification noti = no.get();
			noti.setIsSeen(1);
			notificationRepository.save(noti);
		}
	}

	public StatisticalDTO getDataMonth(String type, String time, Authentication auth) {
		if (type.equalsIgnoreCase("revenue")) {
			if (time.length() < 5) {
				Year year = Year.parse(time);
				SimpleDateFormat formatDay = new SimpleDateFormat("MM");
				List<StatisticalMonth> list = new ArrayList<StatisticalMonth>();
				for (int i = 1; i <= 12; i++) {
					list.add(new StatisticalMonth(i, 0));
				}

				List<Map<String, Object>> listDate = shopRepository.getYear(Common.getIdFromAuth(auth), year.getValue(),
						Common.ORDER_SUCCESS);
				double total = 0.0;
				for (Map<String, Object> i : listDate) {
					int day = Integer.valueOf(i.get("date").toString().substring(0, 2));
					double money = Double.valueOf(i.get("money").toString()) / constants.getDonvi();
					total += ((Double) i.get("money")).floatValue();
					list = list.stream().map((x) -> {
						if (x.getTime() == day) {
							x.setTotal(money);
							x.setTotal((double) Math.round(x.getTotal() * 100) / 100);
						}
						return x;
					}).collect(Collectors.toList());
				}
				return new StatisticalDTO(total, list);

			} else {
				SimpleDateFormat formatDay = new SimpleDateFormat("dd");
				Calendar calendar = Calendar.getInstance();
				DateTimeFormatter f = DateTimeFormatter.ofPattern("MM-uuuu");
				YearMonth ym = YearMonth.parse(time, f);

				calendar.set(ym.getYear(), ym.getMonth().getValue(), 0);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				List<StatisticalMonth> list = new ArrayList<StatisticalMonth>();
				for (int i = 1; i <= maxDay; i++) {
					list.add(new StatisticalMonth(i, 0));
				}

				List<Map<String, Object>> s = shopRepository.getMonth(Common.getIdFromAuth(auth), ym.getYear(),
						ym.getMonthValue(), Common.ORDER_SUCCESS);
				float total = 0f;
				for (Map<String, Object> i : s) {
					int d = Integer.valueOf(i.get("date").toString().substring(0, 2));
					double money = Double.valueOf(i.get("money").toString()) / constants.getDonvi();
					total += Double.valueOf(i.get("total").toString());
					list = list.stream().map((x) -> {
						if (x.getTime() == d) {
							x.setTotal(money);
							x.setTotal((double) Math.round(x.getTotal() * 100) / 100);
						}
						return x;
					}).collect(Collectors.toList());
				}
				return new StatisticalDTO(total, list);
			}
		} else if (type.equalsIgnoreCase("totalOrder")) {

			if (time.length() < 5) {
				Year year = Year.parse(time);

				List<StatisticalMonth> list = new ArrayList<StatisticalMonth>();
				for (int i = 1; i <= 12; i++) {
					list.add(new StatisticalMonth(i, 0));
				}

				List<Map<String, Object>> s = shopRepository.getYear(Common.getIdFromAuth(auth), year.getValue(),
						Common.ORDER_SUCCESS);
				double total = 0.0;
				for (Map<String, Object> i : s) {
					int d = Integer.valueOf(i.get("date").toString().substring(0, 2));
					double number = Double.valueOf(i.get("total").toString());
					total += number;
					list = list.stream().map((x) -> {
						if (x.getTime() == d) {
							x.setTotal(number);
						}
						return x;
					}).collect(Collectors.toList());
				}
				return new StatisticalDTO(total, list);

			} else {
				SimpleDateFormat formatDay = new SimpleDateFormat("dd");
				Calendar calendar = Calendar.getInstance();
				DateTimeFormatter f = DateTimeFormatter.ofPattern("MM-uuuu");
				YearMonth ym = YearMonth.parse(time, f);

				calendar.set(ym.getYear(), ym.getMonth().getValue(), 0);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				List<StatisticalMonth> list = new ArrayList<StatisticalMonth>();
				for (int i = 1; i <= maxDay; i++) {
					list.add(new StatisticalMonth(i, 0));
				}
				List<Map<String, Object>> s = shopRepository.getMonth(Common.getIdFromAuth(auth), ym.getYear(),
						ym.getMonthValue(), Common.ORDER_SUCCESS);
				double total = 0.0;
				for (Map<String, Object> i : s) {
					int d = Integer.valueOf(i.get("date").toString().substring(0, 2));
					double number = Double.valueOf(i.get("total").toString());
					total += number;
					list = list.stream().map((x) -> {
						if (x.getTime() == d)
							x.setTotal(number);
						return x;
					}).collect(Collectors.toList());
				}
				return new StatisticalDTO(total, list);
			}
		}
		return null;
	}

	public void exportExcel(String time, Authentication auth) {
		Shop shop = shopRepository.findById(Common.getIdFromAuth(auth)).get();

		SXSSFWorkbook workbook = new SXSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("Books");

		Row row = null;
		Cell cell = null;
		String date = null;
		Double money = 0.0;
		int quantity = 0;
		double number = 0.0;
		if (time.length() < 5) {
			createHeader(sheet, time);
			List<Map<String, Object>> data = shopRepository.getYear(Common.getIdFromAuth(auth), Integer.valueOf(time),
					Common.ORDER_SUCCESS);
			Double total = 0.0;
			Set<Integer> done = new HashSet<Integer>();
			int day = 0;
			for (Map<String, Object> i : data) {
				date = i.get("date").toString();
				day = Integer.valueOf(date.substring(0, 2));
				done.add(day);
				money = Double.valueOf(i.get("money").toString());
				quantity = Integer.valueOf(i.get("quantity").toString());
				number = Double.valueOf(i.get("total").toString());
				total += Double.valueOf(i.get("money").toString());

				row = sheet.createRow(day);
				cell = row.createCell(0);
				cell.setCellValue(day - 1);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(0);

				cell = row.createCell(1);
				cell.setCellValue(date);
				cell.setCellStyle(getCellStyleDate(sheet, createHelper));
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(1);

				cell = row.createCell(2);
				cell.setCellValue(number);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(2);

				cell = row.createCell(3);
				cell.setCellValue(quantity);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(3);

				cell = row.createCell(4);
				cell.setCellValue(money);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(4);
			}
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell.setCellValue("Tổng:");
			cell.setCellStyle(alignLeft(sheet, createHelper));
			sheet.autoSizeColumn(3);

			cell = row.createCell(4);
			cell.setCellValue(total);
			cell.setCellStyle(alignLeft(sheet, createHelper));
			sheet.autoSizeColumn(4);

			for (int i = 1; i <= 12; i++) {
				if (!done.contains(i)) {
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(i);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(0);

					cell = row.createCell(1);
					cell.setCellValue(i < 10 ? "0" + i + "/" + time : i + "/" + time);
					cell.setCellStyle(getCellStyleDate(sheet, createHelper));
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(1);

					cell = row.createCell(2);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(2);

					cell = row.createCell(3);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(3);

					cell = row.createCell(4);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(4);
				}
			}

		} else {
			createHeader(sheet, time);
			String[] data = time.split("-");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.valueOf(data[1]), Integer.valueOf(data[0]), 0);
			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			List<Map<String, Object>> s = shopRepository.getMonth(Common.getIdFromAuth(auth), Integer.valueOf(data[1]),
					Integer.valueOf(data[0]), Common.ORDER_SUCCESS);
			Set<Integer> done = new HashSet<Integer>();
			double total = 0.0;
			int day = 0;
			for (Map<String, Object> i : s) {
				date = i.get("date").toString();
				day = Integer.valueOf(date.substring(0, 2));
				done.add(day);
				money = Double.valueOf(i.get("money").toString());
				quantity = Integer.valueOf(i.get("quantity").toString());
				number = Double.valueOf(i.get("total").toString());
				total += Double.valueOf(i.get("money").toString());

				row = sheet.createRow(day);
				cell = row.createCell(0);
				cell.setCellValue(day - 1);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(0);

				cell = row.createCell(1);
				cell.setCellValue(date);
				cell.setCellStyle(getCellStyleDate(sheet, createHelper));
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(1);

				cell = row.createCell(2);
				cell.setCellValue(number);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(2);

				cell = row.createCell(3);
				cell.setCellValue(quantity);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(3);

				cell = row.createCell(4);
				cell.setCellValue(money);
				cell.setCellStyle(alignLeft(sheet, createHelper));
				sheet.autoSizeColumn(4);
			}
			row = sheet.createRow(maxDay + 1);
			cell = row.createCell(3);
			cell.setCellValue("Tổng:");
			cell.setCellStyle(alignLeft(sheet, createHelper));
			sheet.autoSizeColumn(3);

			cell = row.createCell(4);
			cell.setCellValue(total);
			cell.setCellStyle(alignLeft(sheet, createHelper));
			sheet.autoSizeColumn(4);

			for (int i = 1; i <= maxDay; i++) {
				if (!done.contains(i)) {
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(i);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(0);

					cell = row.createCell(1);
					cell.setCellValue(i < 10 ? "0" + i + "/" + time : i + "/" + time);
					cell.setCellStyle(getCellStyleDate(sheet, createHelper));
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(1);

					cell = row.createCell(2);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(2);

					cell = row.createCell(3);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(3);

					cell = row.createCell(4);
					cell.setCellValue(0);
					cell.setCellStyle(alignLeft(sheet, createHelper));
					sheet.autoSizeColumn(4);
				}
			}
		}
		try {
			String name = "thongke_" + shop.getId() + ".xlsx";
			String path = constants.getFolder() + File.separator + name;
			createOutputFile(workbook, path);
			emailService.sendFile(shop.getEmail(), path, name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createHeader(Sheet s, String time) {
		Row row = s.createRow(0);
		CellStyle style = getCellStyle(s);
		Cell c = row.createCell(0);
		c.setCellValue("STT");
		c.setCellStyle(style);
		s.autoSizeColumn(0);

		c = row.createCell(1);
		c.setCellValue("Thời gian");
		c.setCellStyle(style);
		s.autoSizeColumn(1);

		c = row.createCell(2);
		c.setCellValue("Số lượng đơn hàng");
		c.setCellStyle(style);
		s.autoSizeColumn(2);

		c = row.createCell(3);
		c.setCellValue("Số lượng sản phẩm");
		c.setCellStyle(style);
		s.autoSizeColumn(3);

		c = row.createCell(4);
		c.setCellValue("Doanh thu(VND)");
		c.setCellStyle(style);
		s.autoSizeColumn(4);
	}

	private CellStyle getCellStyle(Sheet s) {
		Font font = s.getWorkbook().createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 14); // font size
		font.setColor(IndexedColors.WHITE.getIndex());

		XSSFCellStyle cellStyle = (XSSFCellStyle) s.getWorkbook().createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		return cellStyle;
	}

	private CellStyle getCellStyleDate(Sheet s, CreationHelper createHelper) {
		CellStyle cellStyle = s.getWorkbook().createCellStyle();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyy"));
		return cellStyle;
	}

	private CellStyle alignLeft(Sheet s, CreationHelper createHelper) {
		CellStyle cellStyle = s.getWorkbook().createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		return cellStyle;
	}

	private void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
		try (OutputStream os = new FileOutputStream(excelFilePath)) {
			workbook.write(os);
		}
	}

//	public void sum() {
//		List<Order> o = orderRepository.getOrder();
//		double sum = 0;
//		for (Order i : o) {
//				System.out.println(i.getTotalMoney());
//				sum+= i.getTotalMoney();
//		}
//		System.out.println(sum);
//	}
}
