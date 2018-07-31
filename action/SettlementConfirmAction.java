package com.internousdev.retris.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.retris.dao.CartInfoDAO;
import com.internousdev.retris.dao.DestinationInfoDAO;
import com.internousdev.retris.dto.CartInfoDTO;
import com.internousdev.retris.dto.DestinationInfoDTO;
import com.internousdev.retris.dto.PurchaseHistoryInfoDTO;
import com.internousdev.retris.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementConfirmAction extends ActionSupport implements SessionAware{

	private String categoryId;
	private Collection<String> checkList;
	private String productId;
	private String productName;
	private String productNameKana;
	private String imageFilePath;
	private String imageFileName;
	private String price;
	private String releaseCompany;
	private String releaseDate;
	private String productCount;
	private String subtotal;
	private Map<String, Object> session;

	public String execute() {
		String result = ERROR;
		//session.put("loginId", "guest");//テスト後に削除
		//session.put("logined", 1);//テスト後に削除
		session.put("loginIdErrorMessageList", "");
		session.put("passwordErrorMessageList", "");
		session.put("passwordIncorrectMessage", "");
		session.put("goLoginFlg", 0);

		if(session.get("logined").toString().equals("1")) {
			//ログイン済みなら実行
			DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
			List<DestinationInfoDTO> destinationInfoDtoList = new ArrayList<DestinationInfoDTO>();
			try {
				destinationInfoDtoList = destinationInfoDAO.getDestinationInfo(String.valueOf(session.get("loginId")));
				//ログインユーザーの宛先情報を取得
				Iterator<DestinationInfoDTO> iterator = destinationInfoDtoList.iterator();
				if(!(iterator.hasNext())) {
					destinationInfoDtoList = null;
				}
				session.put("destinationInfoDtoList", destinationInfoDtoList);
				//取得できればsessionに配置
			} catch (SQLException e) {
				e.printStackTrace();
			}
			result = SUCCESS;
		}

		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = new ArrayList<PurchaseHistoryInfoDTO>();
		CommonUtility commonUtility = new CommonUtility();
		//parseArrayListメソッドを呼び出すためにインスタンス化
		//ValueStackから取得した情報をカンマで区切り(splitメソッド)各リストへ配置
		if(session.get("createdestinationflg").equals(true)){
			CartInfoDAO cartInfoDao = new CartInfoDAO();
			List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();
			cartInfoDtoList = cartInfoDao.getCartInfoDtoList(String.valueOf(session.get("loginId")));

			for(int i=0;i<cartInfoDtoList.size();i++){
				PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
				purchaseHistoryInfoDTO.setUserId(String.valueOf(session.get("loginId")));
				purchaseHistoryInfoDTO.setProductId(cartInfoDtoList.get(i).getProductId());
				purchaseHistoryInfoDTO.setProductName(cartInfoDtoList.get(i).getProductName());
				purchaseHistoryInfoDTO.setProductNameKana(cartInfoDtoList.get(i).getProductNameKana());
				purchaseHistoryInfoDTO.setImageFilePath(cartInfoDtoList.get(i).getImageFilePath());
				purchaseHistoryInfoDTO.setImageFileName(cartInfoDtoList.get(i).getImageFileName());
				purchaseHistoryInfoDTO.setPrice(cartInfoDtoList.get(i).getPrice());
				purchaseHistoryInfoDTO.setProductCount(cartInfoDtoList.get(i).getProductCount());
				purchaseHistoryInfoDTO.setSubtotal(cartInfoDtoList.get(i).getSubtotal());
				purchaseHistoryInfoDTO.setReleaseCompany(cartInfoDtoList.get(i).getReleaseCompany());
				purchaseHistoryInfoDTO.setReleaseDate(cartInfoDtoList.get(i).getReleaseDate());
				purchaseHistoryInfoDtoList.add(purchaseHistoryInfoDTO);
				session.put("createdestinationflg",false);
			}
		}else{
			String[] productIdList = commonUtility.parseArrayList(productId);
			String[] productNameList = commonUtility.parseArrayList(productName);
			String[] productNameKanaList = commonUtility.parseArrayList(productNameKana);
			String[] imageFilePathList = commonUtility.parseArrayList(imageFilePath);
			String[] imageFileNameList = commonUtility.parseArrayList(imageFileName);
			String[] priceList = commonUtility.parseArrayList(price);
			String[] releaseCompanyList = commonUtility.parseArrayList(releaseCompany);
			String[] releaseDateList = commonUtility.parseArrayList(releaseDate);
			String[] productCountList = commonUtility.parseArrayList(productCount);
			String[] subtotalList = commonUtility.parseArrayList(subtotal);


			for(int i=0; i<productIdList.length; i++) {
				//上記で取得した情報を購入履歴DTOに格納
				PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
				purchaseHistoryInfoDTO.setUserId(String.valueOf(session.get("loginId")));
				purchaseHistoryInfoDTO.setProductId(Integer.parseInt(String.valueOf(productIdList[i])));
				purchaseHistoryInfoDTO.setProductName(String.valueOf(productNameList[i]));
				purchaseHistoryInfoDTO.setProductNameKana(String.valueOf(productNameKanaList[i]));
				purchaseHistoryInfoDTO.setImageFilePath(String.valueOf(imageFilePathList[i]));
				purchaseHistoryInfoDTO.setImageFileName(String.valueOf(imageFileNameList[i]));
				purchaseHistoryInfoDTO.setPrice(Integer.parseInt(String.valueOf(priceList[i])));
				purchaseHistoryInfoDTO.setProductCount(Integer.parseInt(String.valueOf(productCountList[i])));
				purchaseHistoryInfoDTO.setSubtotal(Integer.parseInt(String.valueOf(subtotalList[i])));
				purchaseHistoryInfoDTO.setReleaseCompany(String.valueOf(releaseCompanyList[i]));
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
					purchaseHistoryInfoDTO.setReleaseDate(simpleDateFormat.parse(String.valueOf(releaseDateList[i])));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				purchaseHistoryInfoDtoList.add(purchaseHistoryInfoDTO);
				//DTOをリストに追加
			}
		}
		session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDtoList);
		//リストをsessionに配置 */

		return result;
	}

	public Collection<String> getCheckList() {return checkList;}
	public void setCheckList(Collection<String> checkList) {this.checkList = checkList;}

	public String getProductId() {return productId;}
	public void setProductId(String productId) {this.productId = productId;}

	public String getCategoryId() {return categoryId;}
	public void setCategoryId(String categoryId) {this.categoryId = categoryId;}

	public Map<String, Object> getSession() {return session;}
	public void setSession(Map<String, Object> session) {this.session = session;}

	public String getProductName() {return productName;}
	public void setProductName(String productName) {this.productName = productName;}

	public String getProductNameKana() {return productNameKana;}
	public void setProductNameKana(String productNameKana) {this.productNameKana = productNameKana;}

	public String getImageFilePath() {return imageFilePath;}
	public void setImageFilePath(String imageFilePath) {this.imageFilePath = imageFilePath;}

	public String getImageFileName() {return imageFileName;}
	public void setImageFileName(String imageFileName) {this.imageFileName = imageFileName;}

	public String getPrice() {return price;}
	public void setPrice(String price) {this.price = price;}

	public String getReleaseCompany() {return releaseCompany;}
	public void setReleaseCompany(String releaseCompany) {this.releaseCompany = releaseCompany;}

	public String getReleaseDate() {return releaseDate;}
	public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}

	public String getProductCount() {return productCount;}
	public void setProductCount(String productCount) {this.productCount = productCount;}

	public String getSubtotal() {return subtotal;}
	public void setSubtotal(String subtotal) {this.subtotal = subtotal;}

}