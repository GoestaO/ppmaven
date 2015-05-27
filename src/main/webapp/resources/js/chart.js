/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function extend() {
      this.cfg.axes = {
          xaxis: {
              renderer: $.jqplot.DateAxisRenderer,
              rendererOptions: { tickRenderer: $.jqplot.CanvasAxisTickRenderer },
              tickOptions: {
                  showGridline: true,
                  formatString: '%H:%M',
                  angle: -90                        
             }         
         }
     }
}
