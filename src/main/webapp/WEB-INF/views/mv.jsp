<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3>Memoria verbale</h3>
<spring:bind path="mv01">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dimentico di citare argomenti importanti durante una conversazione</label>
        <div class="col-sm-8">
            <form:select path="mv01" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="mv02">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dimentico cose importanti che avevo detto solo pochi giorni prima</label>
        <div class="col-sm-8">
            <form:select path="mv02" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="mv03">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Sono in grado di ricordare dopo parecchie ore ci&ograve; che &egrave; stato detto al notiziario</label>
        <div class="col-sm-8">
            <form:select path="mv03" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      
<spring:bind path="mv04">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dimentico avvenimenti importanti avvenuti oltre il mese scorso</label>
        <div class="col-sm-8">
            <form:select path="mv04" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mv05">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dimentico parti importanti di chiacchiere (pettegolezzi) interessanti che ho sentito</label>
        <div class="col-sm-8">
            <form:select path="mv05" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mv06">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dimentico di dare messaggi telefonici</label>
        <div class="col-sm-8">
            <form:select path="mv06" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>