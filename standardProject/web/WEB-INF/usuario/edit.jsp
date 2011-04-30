<%@taglib uri="/struts-tags" prefix="s"%>

<h1>Edit Usuario ${usuarioId}</h1>

<div>
<s:if test="usuario != null">
    <s:form action="edit" namespace="/usuario">
        <s:include value="form.jsp" />
    </s:form>
</s:if>
<s:else>
    <s:text name="error.notFound" />
</s:else>
</div>
