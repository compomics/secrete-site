/**
 * Created by demet on 11/9/2017.
 */

$(document).ready(function set(){
    jQuery.noConflict();
    $('#btnSearchProtein').on('click', function () {

        if($('#uniprotAccession').val() != ""){
            if(isValidUniprotAcc($('#uniprotAccession').val())){
                window.location = '/index?protein='+ $('#uniprotAccession').val();
            }else{

                window.location = '/detailedSearch?protein='+ $('#uniprotAccession').val();
            }

        }else{
            var dialogInstance = new BootstrapDialog();
            dialogInstance.setTitle('INPUT ERROR');
            dialogInstance.setMessage('Accession cannot be empty!');
            dialogInstance.setType(BootstrapDialog.TYPE_DANGER);
            dialogInstance.open();
        }
        return false;
    });
    $("#uniprotAccession").keyup(function(event){
        if(event.keyCode == 13){
            $("#btnSearchProtein").click();
        }
    });

    $('#btnSearchDomain').on('click', function () {
        if($('#domainAccession').val() != ""){
            if(isValidDomainAcc($('#domainAccession').val())){
                window.location = '/index?domain='+ $('#domainAccession').val();
            }else{
                window.location = '/detailedSearch?domain='+ $('#domainAccession').val();
            }

        }else{
            var dialogInstance = new BootstrapDialog();
            dialogInstance.setTitle('INPUT ERROR');
            dialogInstance.setMessage('Accession cannot be empty!');
            dialogInstance.setType(BootstrapDialog.TYPE_DANGER);
            dialogInstance.open();
        }
        return false;
    });
    $("#domainAccession").keyup(function(event){
        if(event.keyCode == 13){
            $("#btnSearchDomain").click();
        }
    });


});


    function isValidUniprotAcc(accession){
        if (accession.trim() == "") {
            return false;
        }
        if (accession.length != 6 && accession.length != 10) {
            return false;
        }
        var r = new RegExp("[OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2}", "i");
        if(accession.match(r)){
            return true;
        }
        return false;
    }

    function isValidDomainAcc(accession){
        if (accession.trim() == "") {
            return false;
        }
        if (accession.length != 7 ) {
            return false;
        }
        if(!accession.startsWith("PF")){
            return false;
        }
        var r = new RegExp("[OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2}", "i");
        if(accession.match(r)){
            return true;
        }
        return false;
    }

    function setExample1() {
        document.getElementById('uniprotAccession').value = 'Q15651';
        return false;
    }

    function setExample2() {
        document.getElementById('uniprotAccession').value = 'Q02218';
        return false;
    }
    function setExampleDomain1() {
        document.getElementById('domainAccession').value = 'PF07710';
        return false;
    }

    function setExampleDomain2() {
        document.getElementById('domainAccession').value = 'PF07710';
        return false;
    }
