package com.internousdev.retris.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.retris.dao.PurchaseHistoryInfoDAO;
import com.internousdev.retris.dto.PurchaseHistoryInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeletePurchaseHistoryAction extends ActionSupport implements SessionAware{
	private String categoryId;
	private String sex;
	private List<String> sexList = new ArrayList<String>();
	private static final String MALE = "男性";
	private static final String FEMALE = "女性";
	private String defaultSexValue = MALE;
	private Map<String, Object> session;
	
	public String execute() {
		String result = ERROR;
		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count = purchaseHistoryInfoDAO.deleteAll(String.valueOf(session.get("loginId")));
		//削除実行、データベースから購入履歴が削除され、削除された件数が戻る
		//この時点ではまだsessionから履歴リストは消えてない
		
		if(count > 0) { 
			//DAOで1つ以上削除できた場合に実行
			List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDTOList = purchaseHistoryInfoDAO.getPurchaseHistoryList(String.valueOf(session.get("loginId")));
			Iterator<PurchaseHistoryInfoDTO> iterator = purchaseHistoryInfoDTOList.iterator();
			if(!(iterator.hasNext())) {
				purchaseHistoryInfoDTOList = null;
				//リストを空にする
			}
			session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDTOList);
			//空にしたリストをsessionに配置

			sexList.add(MALE);
			sexList.add(FEMALE);

			result=SUCCESS;
		}
		return result;
	}

	public List<String> getSexList() {return sexList;}
	public void setSexList(List<String> sexList) {this.sexList = sexList;}

	public String getDefaultSexValue() {return defaultSexValue;}
	public void setDefaultSexValue(String defaultSexValue) {this.defaultSexValue = defaultSexValue;}

	public String getCategoryId() {return categoryId;}
	public void setCategoryId(String categoryId) {this.categoryId = categoryId;}

	public String getSex() {return sex;}
	public void setSex(String sex) {this.sex = sex;}

	public Map<String, Object> getSession() {return session;}
	public void setSession(Map<String, Object> session) {this.session = session;}
}
