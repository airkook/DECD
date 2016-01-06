/**
 * Created with IntelliJ IDEA.
 * User: JiangHu
 * Date: 13-11-26
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */

/* $(document).ready(function(){

    $(".tab_content tr:odd").addClass('listtd');
	
});  
*/

 function onloadEvent(func){ 
var one=window.onload 
if(typeof window.onload!='function'){ 
window.onload=func 
} 
else{ 
window.onload=function(){ 
one(); 
func(); 
} 
} 
} 
function showtable(tableid){ 
var overcolor='#FCF9D8'; 
var color1='#ffffff'; 
var color2='#f6f6f6'; 
var tablename=document.getElementById('tab_bianse') 
var tr=tablename.getElementsByTagName("tr") 
for(var i=0 ;i<tr.length;i++){ 
tr[i].onmouseover=function(){ 
this.style.backgroundColor=overcolor; 
} 
tr[i].onmouseout=function(){ 
if(this.rowIndex%2==0){ 
this.style.backgroundColor=color1; 
}else{ 
this.style.backgroundColor=color2; 
} 
} 
if(i%2==0){ 
tr[i].className="color1"; 
}else{ 
tr[i].className="color2"; 
} 
} 
} 
onloadEvent(function init(){ 
showtable('table'); 
showtable('test'); 
} 
); 



$(document).ready(function(){
    var color="#fbfbfb";
    //改变偶数行背景色
    $(".tab_content tr:odd td").css("background-color",color);
    /* 把背景色保存到属性中 */
    $(".tab_content tr:odd").attr("bg",color);
    $(".tab_content tr:even").attr("bg","#fff");

    /* 当鼠标移到表格上是，当前行背景变色 */
    $(".tab_content tr td").mouseover(function(){
        $(this).parent().find("td").css("background-color","#e0ecff");
    });
    /* 当鼠标在表格上移动时，离开的那一行背景恢复 */
    $(".tab_content tr td").mouseout(function(){
        var bgc = $(this).parent().attr("bg");
        $(this).parent().find("td").css("background-color",bgc);
    });
});

