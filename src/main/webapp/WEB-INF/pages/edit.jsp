<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<form method="POST" action="<c:url value="/edit" />" class="form-horizontal"> 
	<fieldset>
		
		<input type="hidden" value="${id}" name="id" /> 
		<div class="form-group">
			<label class="col-sm-2 control-label" for="title"> <s:message code="title" />: </label> 
			<div class="col-sm-6">
				<input class="form-control" type="text" id="title"name="title" value="${titleOfNews}" />
			</div>
		</div> 
		<div class="form-group">
			<label class="col-sm-2 control-label" for="header"> <s:message code="header" />: </label> 
			<div class="col-sm-6">
				<textarea class="form-control" type="text" id="header" name="header" >${headerOfNews}</textarea> 
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="body"> <s:message code="body" />: </label>
			<div class="col-sm-6">
				<textarea class="form-control" type="text" id="body" name="body">${bodyOfNews}</textarea>
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label" for="selectedRegions"><s:message code="regions" />: </label>
			<div class="col-sm-6">
				<div class="input-group input-prepend">
					<span class="input-group-btn">
						<a href="#regionModal" id="addRegion" class="btn btn-default" data-toggle="modal">
							<span class="glyphicon glyphicon-plus"></span>
						</a>
					</span>
					<select class="form-control" id="selectedRegions" name="selectedRegions"	autocomplete="off">
						<c:forEach items="${regions}" var="reg">
							<option value="${reg.id}"
								<c:if test="${reg.isSelected}">selected</c:if>>${reg.name}
							</option>
						</c:forEach>
					</select> 
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label" for="selectedArticles"><s:message code="articles" />: </label>
			<div class="col-sm-6">
				<div class="input-group input-prepend">
					<span class="input-group-btn">
						<a href="#articleModal" id="addArticle" class="btn btn-default" data-toggle="modal">
							<span class="glyphicon glyphicon-plus"></span>
						</a>
					</span>
					<select id="selectedArticles" name="selectedArticles" multiple="multiple" autocomplete="off">
						<c:forEach items="${articles}" var="art">
							<option value="${art.articleId}"
								<c:if test="${art.isSelected}">selected</c:if>>${art.name}
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		
	<div class="col-md-3 col-md-offset-6 btn-group">
		<a class="btn btn-default" href="<c:url value="/"/>"><s:message code="cancel"/></a>
		<button class="btn btn-primary" type="submit"><s:message code="save" /></button>
	</div>
	</fieldset>
</form>

<div class="modal fade" id="regionModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel"><s:message code="regions" /></h4>
      </div>
      <div class="modal-body">
      	<input type="text" class="form-control" name="new-region-name" id="new-region-name"/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="cancel" /></button>
        <button type="button" class="btn btn-primary"><s:message code="save" /></button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="articleModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel"><s:message code="articles" /></h4>
      </div>
      <div class="modal-body">
      	<input type="text" class="form-control" name="new-article-name" id="new-article-name"/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="cancel" /></button>
        <button type="button" class="btn btn-primary"><s:message code="save" /></button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  $(function() {
	  $("select[multiple=multiple]").select2();
	  
	  $("#regionModal button.btn-primary").click(function() {
		  var newRegion = $("#new-region-name").val();
		  if (newRegion.length != 0) {
			  $.ajax({
				  url: "/news/addRegion",
				  data: { name: newRegion },
				  dataType: "json",
				  type: "post",
				  success: function(region) {
					  if (region != null && region.id != 0) {
						  $("#selectedRegions option").removeAttr("selected");
						  $("#selectedRegions").append("<option value='" + region.id + "' selected>" + region.name + "</option>");
						  $("#regionModal").modal('hide');
						  $("#new-region-name").val("");
					  }
					  else {
						  alert("Error");
					  }
				  }
			  });
		  }
	  });
	  
	  $("#articleModal button.btn-primary").click(function() {
		  var newArticle = $("#new-article-name").val();
		  if (newArticle.length != 0) {
			  $.ajax({
				  url: "/news/addArticle",
				  data: { name: newArticle },
				  dataType: "json",
				  type: "post",
				  success: function(article) {
					  if (article != null && article.articleId != 0) {
						  $("#selectedArticles").append("<option value='" + article.articleId + "' selected>" + article.name + "</option>");
						  var select = $("select[multiple=multiple]");
						  select.val(select.val()).trigger("change");
						  $("#articleModal").modal('hide');
						  $("#new-article-name").val("");
					  }
					  else {
						  alert("Error");
					  }
				  }
			  });
		  }
	  });
  });
	  </script>