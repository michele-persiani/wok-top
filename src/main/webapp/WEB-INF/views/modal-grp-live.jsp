<%-- 
    Document   : modal-grp-live
    Created on : May 2, 2017, 3:55:48 PM
    Author     : floriano
--%>

<style>
    #legendlist{
      list-style-type: none; 
    }
    #legendlist li {
      margin-bottom: 3px;
    }
    #legendlist > li > :first-child{
      margin-right: 15px;
    }
</style>

<!-- Modal -->
<div id="infoModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span class="glyphicon glyphicon-remove-circle"></span>
                </button>
                <h3 class="modal-title">Riabilitazione cognitiva</h3>
            </div>
            <div class="modal-body">
                <h4>Qui puoi vedere in tempo reale come un gruppo di <!--pazienti-->partecipanti sta svolgendo gli esercizi.</h4>
                <h4>In ciascuna riga trovi il nome del <!--paziente-->partecipante, quello dell'esercizio, l'attuale livello di difficolt&agrave;, l'ultima performance, e l'ultimo orario in cui il <!--paziente-->partecipante ha eseguito l'esercizio.</h4>
                <h4>La performance di un eserizio di difficolt&agrave; "facile" varia dello 0% al 33%, quella di un esercizio "medio" dallo 0% al 66%, quella di un esercizio "difficile" dallo 0% al 100%.</h4>
                <h4>&Egrave; possibile aumentare o diminuire manualmente il livello di difficolt&agrave; dell'esercizio.</h4>
                <hr />
                <h4 >Legenda</h4>
                <ul id="legendlist">
                    <li><span class="glyphicon glyphicon-arrow-up padding-arrow" aria-hidden="true"></span> il <!--paziente-->partecipante è migliorato rispetto al precedente esercizio.</li>
                    <li><span class="glyphicon glyphicon-resize-horizontal padding-arrow" aria-hidden="true"></span> il <!--paziente-->partecipante non ha variato la performance rispetto al precedente esercizio.</li>
                    <li><span class="glyphicon glyphicon-arrow-down padding-arrow" aria-hidden="true"></span> il <!--paziente-->partecipante è peggiorato rispetto al precedente esercizio.</li>
                    <li><button type="button" class="btn btn-sm btn-danger" aria-label="Abbassa difficoltà"> <span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button> bottone per abbassare il livello di difficoltà del prossimo esercizio.</li>
                    <li><button type="button" class="btn btn-sm btn-success" aria-label="Abbassa difficoltà"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button> bottone per aumentare il livello di difficoltà del prossimo esercizio.</li>
                    <li><input id="" class="short " type="text" readonly value="2/10" /> Rappresenta il livello di difficoltà al quale è stato svolto l'esercizio (2) e il suo massimale possibile (10).</li>
                    <li><input id="" class="short red" type="text" readonly value="4/10" /> Se il colore è arancione significa che il valore (4) è stato modificato per il prossimo esercizio.</li>
                </ul>
                <hr>
                <h3 style='color: red'><b>Questo sistema &egrave; destinato esclusivamente ad indagine clinica</b></h3>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">Ok</button>
            </div>        
        </div>
    </div>
</div>