// Compiled by ClojureScript 1.10.238 {}
goog.provide('client.dash');
goog.require('cljs.core');
client.dash.menu_burger = document.getElementById("hamburger");
client.dash.contents = document.getElementById("contents");
client.dash.tbl_ctrl_bar = document.getElementById("tbl-ctrl-bar");
client.dash.side_bar = document.getElementById("side-bar");
client.dash.show_mobile_menu = (function client$dash$show_mobile_menu(){
client.dash.menu_burger.style.display = "none";

client.dash.contents.style.display = "none";

client.dash.tbl_ctrl_bar.style.display = "none";

return client.dash.side_bar.style.display = "block";
});
client.dash.add_click_event = (function client$dash$add_click_event(el){
return el.addEventListener("click",client.dash.show_mobile_menu);
});
client.dash.setup = (function client$dash$setup(){
return client.dash.add_click_event.call(null,client.dash.menu_burger);
});
client.dash.setup.call(null);

//# sourceMappingURL=dash.js.map
