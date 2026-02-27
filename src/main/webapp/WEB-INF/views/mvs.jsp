<%-- any content can be specified here e.g.: --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<h3>Memoria visuo-spaziale</h3>
<spring:bind path="mvs01">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dopo che ho messo qualcosa da parte sono in grado di ricordare il posto</label>
        <div class="col-sm-8">
            <form:select path="mvs01" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="mvs02">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Se vado per la prima volta in un ristorante, riesco facilmente a tornare al mio posto dopo essermi alzato</label>
        <div class="col-sm-8">
            <form:select path="mvs02" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>                      

<spring:bind path="mvs03">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Ho difficolt&agrave; a trovare un negozio in un centro commerciale dove sono gi&agrave; stato altre volte</label>
        <div class="col-sm-8">
            <form:select path="mvs03" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mvs04">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Posso localizzare facilmente un oggetto che so trovarsi nel mio armadio</label>
        <div class="col-sm-8">
            <form:select path="mvs04" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mvs05">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Trovo difficile ricordarmi le facce di persone che ho incontrato recentemente</label>
        <div class="col-sm-8">
            <form:select path="mvs05" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mvs06">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Dopo la visita ad un posto sono in grado di ritrovarlo con scarsa difficolt&agrave; (ristorante, negozio)</label>
        <div class="col-sm-8">
            <form:select path="mvs06" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mvs07">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Ricordo le fotografie che accompagnano i settimanali o i quotidiani che ho letto recentemente</label>
        <div class="col-sm-8">
            <form:select path="mvs07" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>

<spring:bind path="mvs08">
    <div class="form-group ${status.error ? 'has-error' : ''}">
        <label class="col-sm-16 control-label">Posso facilmente trovare il mio cappotto in mezzo ad altri in un guardaroba</label>
        <div class="col-sm-8">
            <form:select path="mvs08" class="form-control">
                <form:option value="MAI">Mai</form:option>
                <form:option value="DIRADO">Di rado</form:option>
                <form:option selected="selected" value="OGNITANTO">Ogni tanto</form:option>
                <form:option value="SPESSO">Spesso</form:option>
                <form:option value="SEMPRE">Sempre</form:option>                                    
            </form:select>
        </div>
    </div>
</spring:bind>
