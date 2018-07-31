package com.internousdev.retris.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.retris.dto.PurchaseHistoryInfoDTO;
import com.internousdev.retris.util.DBConnector;

public class PurchaseHistoryInfoDAO {
//購入履歴登録
	public int regist(String loginId, int productId, int productCount, int destinationId, int price) {
		//購入した商品情報をpurchase_history_infoに格納 (決済完了Actionで呼び出される)
		int count = 0;
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		String sql = "INSERT INTO purchase_history_info(user_id, product_id, product_count, price, destination_id,regist_date, update_date, delete_flg) "
						+ "VALUES (?, ?, ?, ?, ?, now(), '0000-01-01', 0)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginId);
			preparedStatement.setInt(2, productId);
			preparedStatement.setInt(3, productCount);
			preparedStatement.setInt(4, price);
			preparedStatement.setInt(5, destinationId);
			count = preparedStatement.executeUpdate();
			//格納した件数をcountに代入
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(!(connection == null)) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
		//格納した件数を戻す
	}

//購入履歴取得
	public List<PurchaseHistoryInfoDTO> getPurchaseHistoryList(String loginId) {
		//sessionに配置されているloginIdに一致し、delete_flgが0(表示)の購入履歴情報をDBから取り出す
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDTOList = new ArrayList<PurchaseHistoryInfoDTO>();
		String sql = "SELECT phi.id as id," /* ID */
								+ " phi.user_id as user_id," /* ユーザーID */
								+ " phi.product_count as product_count," /* 個数 */
								+ " pi.product_id as product_id," /* 商品ID */
								+ " pi.product_name as product_name," /*商品名*/
								+ " pi.product_name_kana as product_name_kana," /* 商品名かな */
								+ " pi.product_description as product_description," /* 商品詳細 */
								+ " pi.category_id as category_id," /* カテゴリID */
								+ " pi.price," /* 価格 */
								+ " pi.image_file_name as image_file_name," /* 画像ファイルパス */
								+ " pi.image_file_path as image_file_path," /* 画像ファイル名 */
								+ " pi.release_company," /* 発売会社名 */
								+ " pi.release_date," /* 発売年月日 */
								+ " phi.price as price," /* 値段 */
								+ " phi.regist_date as regist_date," /* 登録日 */
								+ " phi.update_date as update_date," /* 更新日 */
								+ " di.family_name as family_name," /* 姓 */
								+ " di.first_name as first_name," /* 姓 */
								+ " di.family_name_kana as family_name_kana," /* 姓かな */
								+ " di.first_name_kana as first_name_kana," /* 名かな */
								+ " di.email as email," /* メールアドレス */
								+ " di.tel_number as tel_number," /* 電話番号 */
								+ " di.user_address as user_address" /* 住所 */
					+ " FROM purchase_history_info as phi"
					+ " LEFT JOIN product_info as pi"
					+ " ON phi.product_id = pi.product_id"
					+ " LEFT JOIN destination_info as di"
					+ " ON phi.destination_id = di.id"
					+ " WHERE delete_flg = 0 AND phi.user_id = ?" //delete_flgカラムが0(表示)のレコードのみ
					+ " ORDER BY regist_date DESC";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				//取り出した購入履歴情報をDTOに格納し、リストに配列
				PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
				purchaseHistoryInfoDTO.setId(resultSet.getInt("id"));
				purchaseHistoryInfoDTO.setUserId(resultSet.getString("user_id"));
				purchaseHistoryInfoDTO.setProductId(resultSet.getInt("product_id"));
				purchaseHistoryInfoDTO.setProductCount(resultSet.getInt("product_count"));
				purchaseHistoryInfoDTO.setPrice(resultSet.getInt("price"));
				purchaseHistoryInfoDTO.setRegistDate(resultSet.getDate("regist_date"));
				purchaseHistoryInfoDTO.setUpdateDate(resultSet.getDate("update_date"));
				purchaseHistoryInfoDTO.setProductName(resultSet.getString("product_name"));
				purchaseHistoryInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
				purchaseHistoryInfoDTO.setProductDescription(resultSet.getString("product_description"));
				purchaseHistoryInfoDTO.setCategoryId(resultSet.getInt("category_id"));
				purchaseHistoryInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
				purchaseHistoryInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
				purchaseHistoryInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
				purchaseHistoryInfoDTO.setReleaseDate(resultSet.getDate("release_date"));
				purchaseHistoryInfoDTO.setFamilyName(resultSet.getString("family_name"));
				purchaseHistoryInfoDTO.setFirstName(resultSet.getString("first_name"));
				purchaseHistoryInfoDTO.setFamilyNameKana(resultSet.getString("family_name_kana"));
				purchaseHistoryInfoDTO.setFirstNameKana(resultSet.getString("first_name_kana"));
				purchaseHistoryInfoDTO.setEmail(resultSet.getString("email"));
				purchaseHistoryInfoDTO.setTelNumber(resultSet.getString("tel_number"));
				purchaseHistoryInfoDTO.setUserAddress(resultSet.getString("user_address"));
				purchaseHistoryInfoDTOList.add(purchaseHistoryInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(!(connection == null)) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return purchaseHistoryInfoDTOList;
	}

//購入履歴削除
	public int deleteAll(String loginId) {
		//該当する購入履歴のdelete_flgを1(非表示)に変更
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		String sql = "UPDATE purchase_history_info SET delete_flg=1 WHERE user_id=?";
		int count = 0;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginId);
			count = preparedStatement.executeUpdate();
			//非表示にした件数を代入
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(!(connection == null)) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}





	//以下ランキングについて
	//個数のランキングにするということなので個数で並べ替えてます
	//単体価格を「price」から「singleprice」におきかえてます　←jspやactionには関係なし
	public List<PurchaseHistoryInfoDTO> getRPGrankList(){

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<PurchaseHistoryInfoDTO> rankList  = new ArrayList<PurchaseHistoryInfoDTO>();
		String sql="select *,pi.price as singleprice, sum(phi.product_count)from purchase_history_info as phi left join product_info as pi on phi.product_id = pi.product_id where pi.category_id = 2 group by phi.product_id order by sum(phi.product_count) desc limit 3";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PurchaseHistoryInfoDTO dto = new PurchaseHistoryInfoDTO();
				dto.setProductName(rs.getString("product_name"));
				dto.setPrice(rs.getInt("singleprice"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setProductId(rs.getInt("product_id"));
				rankList.add(dto);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rankList;
	}

	public List<PurchaseHistoryInfoDTO> getShotrankList(){

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<PurchaseHistoryInfoDTO> rankList  = new ArrayList<PurchaseHistoryInfoDTO>();
		String sql="select *,pi.price as singleprice, sum(phi.product_count)from purchase_history_info as phi left join product_info as pi on phi.product_id = pi.product_id where pi.category_id = 3 group by phi.product_id order by sum(phi.product_count) desc limit 3";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PurchaseHistoryInfoDTO dto = new PurchaseHistoryInfoDTO();
				dto.setProductName(rs.getString("product_name"));
				dto.setPrice(rs.getInt("singleprice"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setProductId(rs.getInt("product_id"));
				rankList.add(dto);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rankList;
	}


	public List<PurchaseHistoryInfoDTO> getPuzzlerankList(){

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<PurchaseHistoryInfoDTO> rankList  = new ArrayList<PurchaseHistoryInfoDTO>();
		String sql="select *,pi.price as singleprice, sum(phi.product_count)from purchase_history_info as phi left join product_info as pi on phi.product_id = pi.product_id where pi.category_id = 4 group by phi.product_id order by sum(phi.product_count) desc limit 3";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PurchaseHistoryInfoDTO dto = new PurchaseHistoryInfoDTO();
				dto.setProductName(rs.getString("product_name"));
				dto.setPrice(rs.getInt("singleprice"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setProductId(rs.getInt("product_id"));
				rankList.add(dto);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rankList;
	}


	//ランキング用商品削除メソッド

		public int deleteProduct(int productId) {
			DBConnector dbConnector = new DBConnector();
			Connection connection = dbConnector.getConnection();
			int count = 0;
			String sql = "delete from purchase_history_info where product_id=?";

			try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, productId);

				count = preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return count;
		}
}

