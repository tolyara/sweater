<#import "parts/common.ftl" as c> 

<@c.page>
<h3>${userChannel.username}</h3>
<#if !isCurrentUser>
	<#if isSubscriber>
		<a class="btn btn-danger" href="/user/unsubscribe/${userChannel.id}">UnSubscribe</a>
	<#else>
		<a class="btn btn-success" href="/user/subscribe/${userChannel.id}">Subscribe</a>
	</#if>
</#if>
<div class="container my-3">
	<div class="row">
		<div class="col">
			<div class="card">
				<div class="card-body">
					<div class="card-title">Subscriptions</div>
					<h3 class="card-text">
						<a href="/user/subscriptions/${userChannel.id}/list">${subscriptionsCount}</a>
					</h3>
				</div>
			</div>
		</div>
		<div class="col">
			<div class="card"></div>	
				<div class="card-body">
					<div class="card-title">Subscribers</div>
					<div class="card-text">
						<a href="/user/subscribers/${userChannel.id}/list">${subscribersCount}</a>
					</div>
				</div>
		</div>
	</div>
</div>
<#if isCurrentUser>
	<#include "parts/messageEdit.ftl"/>  <!-- #include directive pasts code right into this place -->
</#if>
<#include "parts/messageList.ftl"/> 

</@c.page>