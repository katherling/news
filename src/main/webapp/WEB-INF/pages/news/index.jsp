<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>



<form action='<c:url value="/" />'>
	<s:message code="regions" />: <select name="selectedRegions" multiple="multiple"	autocomplete="off">
		<c:forEach items="${regions}" var="reg">
			<option value="${reg.id}"
				<c:if test="${reg.isSelected}">selected</c:if>>${reg.name}
			</option>
		</c:forEach>
	</select> <s:message code="articles" />: <select name="selectedArticles" multiple="multiple" autocomplete="off">
		<c:forEach items="${articles}" var="art">
			<option value="${art.articleId}"
				<c:if test="${art.isSelected}">selected</c:if>>${art.name}
			</option>
		</c:forEach>
	</select> <a href="#" class="submit btn btn-primary"><s:message code="filter" /></a>
</form>

<h2><s:message code="news" /></h2>
<c:forEach items="${newsList}" var="item">
	<div class="newsItem well well-sm" data-id="${item.id}">
		<h3><a href="#" class="title">${item.title}</a></h3>
		<div class="text-muted">
			<span class="btn-group">
			<button class="btn btn-default btn-xs" disabled>${item.date}</button>
			<a class="btn btn-xs btn-primary" href="<c:url value="/?selectedRegions=${item.region.id}" />">
				${item.region.name}
			</a>	 
			<c:forEach items="${item.articles}" var="art">
				<a class="btn btn-xs btn-default" href="<c:url value="/?selectedArticles=${art.articleId}"/>">${art.name}</a>
			</c:forEach>
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
					<a class="btn btn-default btn-xs" href="<c:url value="/delete?id=${item.id}" />" title="<s:message code="delete" />">
						<span class="glyphicon glyphicon-remove"></span>
					</a>
					<a class="btn btn-default btn-xs" href="<c:url value="/edit?id=${item.id}" />" title="<s:message code="edit" />">
						<span class="glyphicon glyphicon-pencil"></span>
					</a>
				</sec:authorize>
				
			</span>
		</div>
		<div><h5 class="text-info"><em>${item.header}</em></h5></div>
		<div class="bodyContainer" style="display: none"></div>
		<div></div>
	</div>
	<br />
</c:forEach>

<ul class="pagination">
	<li class="<c:if test="${currentPage == 1}">disabled</c:if>">
		<a href="#" data-page="${currentPage - 1}">
			&laquo;
		</a>
	</li>
	<c:forEach var="i" begin="1" end="${totalPages}">
		<li class="<c:if test="${i == currentPage}">active</c:if>">
			<a href="#" data-page="${i}">
				${i}
			</a>
		</li>
	</c:forEach>
	<li class="<c:if test="${currentPage == totalPages}">disabled</c:if>">
		<a href="#" data-page="${currentPage + 1}">
			&raquo;
		</a>
	</li>
</ul>



<script type="text/javascript">
	
function insertParam(key, value)
{
    key = encodeURI(key); value = encodeURI(value);

    var kvp = document.location.search.substr(1).split('&');

    var i=kvp.length; var x; while(i--) 
    {
        x = kvp[i].split('=');

        if (x[0]==key)
        {
            x[1] = value;
            kvp[i] = x.join('=');
            break;
        }
    }

    if(i<0) {kvp[kvp.length] = [key,value].join('=');}

    //this will reload the page, it's likely better to store this until finished
    return "?" + kvp.join('&'); 
}

  $(function() {
	  $("select[multiple=multiple]").select2();
	  
	$("div.newsItem a.title").click(
				function() {
					var item = $(this).parent().parent();
					var id = $(item).data("id");
					var bodyContainer = $(item).find("div.bodyContainer");
					if (bodyContainer.hasClass("open")) {
						bodyContainer.removeClass("open").slideUp();
						
					} else {
						$.ajax({
							url : "/news/getNews.json",
							data: { id: id },
							dataType : "json",
							type : "post",
							success : function(data) {
								bodyContainer
										.html("<p>" + data.body + "</p>")
										.addClass("open")
										.slideDown();
							}
						});
					}
					
					return false;
				});
		$("form > a.submit").click(function() {
			$(this).parent().submit();
		});
		
		$(".pagination a").each(function() {
			var page = $(this).data("page");
			if (page != 0) {
				$(this).attr("href", insertParam("page", page));
			}
			
		});
		
		$("li.disabled a")
			.attr("href", "#")
			.click(function() { return false; });
		

	});
</script>

