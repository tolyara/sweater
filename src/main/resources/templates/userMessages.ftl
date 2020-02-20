<#import "parts/common.ftl" as c> 

<@c.page>
<#if isCurrentUser>
	<#include "parts/messageEdit.ftl"/>  <!-- #include directive pasts code right into this place -->
</#if>
<#include "parts/messageList.ftl"/> 

</@c.page>