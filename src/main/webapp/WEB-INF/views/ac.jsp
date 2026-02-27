<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3>Attenzione-concentrazione</h3>
<spring:bind path="ac01">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Sono in grado di fare calcoli semplici</label>
        <div class="col-sm-8">
            <form:select path="ac01" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="ac02">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Chiedo spesso alle persone di ripetere quanto hanno detto perch&egrave; durante la conversazione la mia testa vaga</label>
        <div class="col-sm-8">
            <form:select path="ac02" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="ac03">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Sto attento a quanto mi accade intorno</label>
        <div class="col-sm-8">
            <form:select path="ac03" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      
<spring:bind path="ac04">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Ho difficolt&agrave; a stare fermo seduto per guardare i miei programmi televisivi preferiti</label>
        <div class="col-sm-8">
            <form:select path="ac04" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="ac05">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Vengo facilmente distratto nel mio lavoro dalle cose che stanno intorno a me</label>
        <div class="col-sm-8">
            <form:select path="ac05" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="ac06">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Riesco a tenere a mente pi&ugrave; cose contemporaneamente</label>
        <div class="col-sm-8">
            <form:select path="ac06" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="ac07">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Riesco a focalizzare la mia attenzione su una prova per solo pochi minuti alla volta</label>
        <div class="col-sm-8">
            <form:select path="ac07" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="ac08">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Ho difficolt&agrave; a riprendere il filo dei miei pensieri anche dopo una breve interruzione</label>
        <div class="col-sm-8">
            <form:select path="ac08" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      
