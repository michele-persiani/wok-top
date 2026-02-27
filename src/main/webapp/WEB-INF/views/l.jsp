<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3>Linguaggio</h3>
<spring:bind path="l01">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label pull-left">Parlando ho difficolt&agrave; a comunicare le cose</label>
        <div class="col-sm-8">
            <form:select path="l01" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="l02">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Posso eseguire una conversazione al telefono</label>
        <div class="col-sm-8">
            <form:select path="l02" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="l03">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Mi devo fermare per trovare le parole per esprimere i miei pensieri</label>
        <div class="col-sm-8">
            <form:select path="l03" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="l04">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Il mio linguaggio &egrave; lento ed esitante</label>
        <div class="col-sm-8">
            <form:select path="l04" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="l05">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Mi capita di dare un nome sbagliato ad un oggetto familiare</label>
        <div class="col-sm-8">
            <form:select path="l05" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="l06">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Trovo facile capire quello che la gente mi dice</label>
        <div class="col-sm-8">
            <form:select path="l06" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="l07">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Mi sembra che la gente parli troppo rapidamente</label>
        <div class="col-sm-8">
            <form:select path="l07" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="l08">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Mi &egrave; facile leggere e seguire l'articolo di un giornale</label>
        <div class="col-sm-8">
            <form:select path="l08" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="l09">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Devo udire o leggere qualcosa varie volte prima di potermene ricordare senza difficolt&agrave;</label>
        <div class="col-sm-8">
            <form:select path="l09" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="l10">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Posso richiamare i nomi di persone che erano famose quando ero piccolo</label>
        <div class="col-sm-8">
            <form:select path="l10" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>            
