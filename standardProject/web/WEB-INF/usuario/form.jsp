<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/struts-jquery-tags" prefix="sj" %>

<div id="messages">
<s:actionerror />
</div>

<s:push value="usuario">
    <s:hidden name="id" />
    <p>
        <s:label key="label.nome" />
        <s:textfield name="nome" />
        <s:fielderror fieldName="nome" />
    </p>
    <p>
        <s:label key="label.endereco" />
        <s:textfield name="endereco" />
        <s:fielderror fieldName="endereco" />
    </p>
    <p>
        <s:label key="label.idade" />
        <s:textfield name="idade" />
        <s:fielderror fieldName="idade" />
    </p>
    <p>
        <s:label key="label.nascimento" />
        <sj:datepicker name="nascimento" displayFormat="dd/mm/yy" />
        <s:fielderror fieldName="nascimento" />
    </p>
    <p>
        <s:hidden name="save" value="true" />
        <s:submit key="label.save" name="" />
        <s:submit key="form.cancel" onclick="window.location.href = './index.action';return false;" />
    </p>
</s:push>