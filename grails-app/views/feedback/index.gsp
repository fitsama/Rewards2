<!DOCTYPE html>
<html>
<head>
<title> Feedback From </title>
	<meta charset="utf-8" content="main" name="layout">
</head>
<body>
<div class="nav" role="navigation">
<ul>
<li><a class="home" href="${createLink(uri:'/')}"><g:message code="default.home.label"/>Home</a></li>
    	</ul>
</div>
	<div id="create-feedbackForm" class="content scaffold-create" role="main">
		<g:message code="Feedback From"/>
<g:form action="index">
<fieldset class="form">
<g:render template="form"/>
</fieldset>
<fieldset class="buttons">
<g:submitButton name="displayForm" class="save" value="Submit"/>
				</fieldset>
</g:form>
	</div>
</body>
</html>