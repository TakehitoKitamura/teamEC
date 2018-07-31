package com.internousdev.retris.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.retris.dao.MCategoryDAO;
import com.internousdev.retris.dao.PurchaseHistoryInfoDAO;
import com.internousdev.retris.dto.MCategoryDTO;
import com.internousdev.retris.dto.PurchaseHistoryInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class PurchaseHistoryAction extends ActionSupport implements SessionAware{
	private String categoryId;
	private List<MCategoryDTO> mCategoryDtoList = new ArrayList<MCategoryDTO>();
	private Map<String, Object> session;

	public String execute() {
		PurchaseHistoryInfoDAO purchaseHistoryInfoDao = new PurchaseHistoryInfoDAO();
		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = new ArrayList<PurchaseHistoryInfoDTO>();
		purchaseHistoryInfoDtoList = purchaseHistoryInfoDao.getPurchaseHistoryList(String.valueOf(session.get("loginId")));
		//購入した商品情報をDTOに格納し、リストに配列
		Iterator<PurchaseHistoryInfoDTO> iterator = purchaseHistoryInfoDtoList.iterator();
		//イテレーターをインスタンス化
		if(!(iterator.hasNext())) {
			purchaseHistoryInfoDtoList = null;
		}
		//購入リストが空の場合、nullを代入
		session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDtoList);
		//sessionに購入履歴リストを配置

		if(!session.containsKey("mCategoryList")) {
			MCategoryDAO mCategoryDao = new MCategoryDAO();
			mCategoryDtoList = mCategoryDao.getMCategoryList();
			session.put("mCategoryDtoList", mCategoryDtoList);
		}
		return SUCCESS;
	}


	public List<MCategoryDTO> getmCategoryDtoList() {return mCategoryDtoList;}
	public void setmCategoryDtoList(List<MCategoryDTO> mCategoryDtoList) {this.mCategoryDtoList = mCategoryDtoList;}

	public String getCategoryId() {return categoryId;}
	public void setCategoryId(String categoryId) {this.categoryId = categoryId;}

	public Map<String, Object> getSession() {return session;}
	public void setSession(Map<String, Object> session) {this.session = session;}
}
