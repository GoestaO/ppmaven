/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function finishAll() {
//    $(":radio[value=false]").click();
    $(".statusButton :radio[value=false]").click();
}

function openAll() {
    $(".statusButton :radio[value=true]").click();
}

function colorizeRadioButtons() {

    // Klick auf offen: offen auf grün setzen, fertig zurücksetzen
    $(".statusButton input[value=true]").closest('.ui-state-active').css("background-color", "#ff0000");
    $(".statusButton input[value=true]").closest('.ui-button').css("background-color", "");

// Klick auf fertig: offen zurücksetzen, fertig auf rot setzen
    $(".statusButton input[value=false]").closest('.ui-state-active').css("background-color", "#008000");
    $(".statusButton input[value=false]").closest('.ui-button').css("background-color", "");

    // Initiale Zustände: offen = grün, fertig = rot
    $(".statusButton input[value=true]").closest('.ui-state-active').css("background-color", "#ff0000");
    $(".statusButton input[value=false]").closest('.ui-state-active').css("background-color", "#008000");


}

PrimeFaces.widget.Dialog.prototype.applyFocus = function () {
    this.jq.find(':not(:submit):not(:button):input:visible:enabled:first').blur();
}
