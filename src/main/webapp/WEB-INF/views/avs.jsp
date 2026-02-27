<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3>Abilit&agrave; visuo-spaziali</h3>
<spring:bind path="avs01">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Sono in grado di mettere insieme i pezzi di un puzzle</label>
        <div class="col-sm-8">
            <form:select path="avs01" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="avs02">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Sono capace di usare le istruzioni per costruire oggetti da solo (bricolage)</label>
        <div class="col-sm-8">
            <form:select path="avs02" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="avs03">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Ho difficolt&agrave; a focalizzare un amico in mezzo ad una folla</label>
        <div class="col-sm-8">
            <form:select path="avs03" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="avs04">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Trovo difficolt&agrave; a stimare le distanze (ad esempio, tra casa mia e quella di un parente)</label>
        <div class="col-sm-8">
            <form:select path="avs04" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="avs05">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Mi perdo quando viaggio</label>
        <div class="col-sm-8">
            <form:select path="avs05" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="avs06">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">&Egrave; facile per me leggere una carta geografica e trovare una nuova localit&agrave;</label>
        <div class="col-sm-8">
            <form:select path="avs06" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>