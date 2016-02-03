/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function onOpen(evt) {
    console.log("START: " + evt.data);

}
function onClose(evt) {
}

function onMessage(evt) {
    console.log(evt.data);
}

function onError(evt) {
    console.log("ERROR: " + evt.data);

}