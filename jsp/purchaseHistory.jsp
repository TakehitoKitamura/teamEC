<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="./css/style.css">
	<link rel="stylesheet" href="./css/botton_style.css">
	<link rel="stylesheet" href="./css/purchaseHistory_style.css">

	<title>商品購入履歴</title>

</head>
<body>
<jsp:include page="header.jsp" />
<div id="contents">
<div id="ordinal">
<div id="box-wide">

		<div class="title_box">
		<h1>商品購入履歴</h1>
		</div>
		<s:if test="#session.purchaseHistoryInfoDtoList.size()>0">
			<table class="horizontal-list-table" border="1">
				<thead>
					<tr>
						<th>商品画像</th>
						<th>商品詳細</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="#session.purchaseHistoryInfoDtoList">
					<tr>
						<td><img src='<s:property value="%{imageFilePath}"/>/<s:property value="%{imageFileName}"/>' class="image_box"/></td>
						<td>
							<div  class="details_list">
									<div class="list">商品名&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="productName"/></div>
									<div class="list">ふりがな&nbsp;&nbsp;<s:property value="productNameKana"/></div>
									<div class="list">値段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="price"/>円</div>
									<div class="list">発売会社&nbsp;&nbsp;<s:property value="releaseCompany"/></div>
									<div class="list">発売日&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="releaseDate"/></div>
							</div>
						</td>
					</tr>
					</s:iterator>
				</tbody>
			</table>
			<div class="footer">
            <s:form action="DeletePurchaseHistoryAction">

                <div  class="submit_btn_box">
                <div class="button_base b05_3d_roll">
				<s:submit value="履歴をすべて削除"  class="submit" />
				<s:submit value="履歴をすべて削除"  class="submit" />
				</div>
				</div>

            </s:form>
        </div>
		</s:if>
		<s:else>
			<div class="info">
			商品購入履歴情報はありません。
			</div>
		</s:else>


</div>
</div>
</div>
<s:include value="footer.jsp"/>
</body>
</html>