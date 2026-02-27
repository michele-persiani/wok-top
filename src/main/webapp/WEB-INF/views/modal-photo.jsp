<%-- 
    Document   : modal-photo
    Created on : Jan 10, 2017, 8:35:48 PM
    Author     : floriano
--%>

<!-- Modal -->
<div id="photoModal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span class="glyphicon glyphicon-remove-circle"></span>
                </button>
                <!--h4 id="patient" class="modal-title">Riabilitazione cognitiva</h4--> 
                <h4 id="patient" class="modal-title">Training cognitivo</h4>
                <hr>
                <h3 style='color: red'><b>Questo sistema &egrave; destinato esclusivamente ad indagine clinica</b></h3>
            </div>
            <div class="modal-body">
                <img id="photo" src="" class="img-circle img-responsive center-block" width="200" height="200" alt="photo">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">Ok</button>
            </div>        
        </div>
    </div>
</div>