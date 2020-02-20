<#import "parts/common.ftl" as c> <@c.page>

<div class="form-row">
	<div class="form-group col-md-6">
		<form method="get" action="/main" class="form-inline">
			<input type="text" name="filter" value="${filter?ifExists}"
				class="form-control" placeholder="Search by tag">
			<button type="submit" class="btn btn-primary ml-2">Search</button>
		</form>
	</div>
</div>

<a class="btn btn-primary" data-toggle="collapse"
	href="#collapseExample" role="button" aria-expanded="false"
	aria-controls="collapseExample"> Add new message </a>

<div class="collapse <#if message??>show</#if>" id="collapseExample">
	<div class="form-group mt-3">
		<form method="post" enctype="multipart/form-data">
			<div class="form-group">
				<input type="text" name="text" class="form-control ${ (textError??)?string('is-invalid', '') }"
					value="<#if message??> ${message.text} </#if>" placeholder="Введите сообщение" />
				<#if textError??>
				<div class="invalid-feedback">
        			${textError}
      			</div>
      			</#if>
			</div>
			<div class="form-group">
				<input type="text" value="<#if message??> ${message.tag} </#if>" name="tag" class="form-control" placeholder="Тэг">
				<#if tagError??>
				<div class="invalid-feedback">
        			${tagError}
      			</div>
      			</#if>
			</div>
			<div class="form-group">
				<div class="custom-file">
					<input type="file" name="file" id="customFile"> <label
						class="custom-file-label" for="customFile"></label>
				</div>
			</div>
			<input type="hidden" name="_csrf" value="${_csrf.token}" />
			<div class="form-group">
				<button type="submit" class="btn btn-primary">Добавить</button>
			</div>
		</form>
	</div>
</div>
<br>

<#include "parts/messageList.ftl"/>  <!-- #include directive pasts code right into this place -->

</@c.page> 








