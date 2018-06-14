// Compiled by ClojureScript 1.10.238 {}
goog.provide('client.dash');
goog.require('cljs.core');
goog.require('reagent.core');
goog.require('ajax.core');
goog.require('cljs.reader');
goog.require('client.auth');
/**
 * User's data.
 */
client.dash.userdata = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
/**
 * Active table data.
 */
client.dash.table = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
client.dash.get_user_data = (function client$dash$get_user_data(){
return ajax.core.GET.call(null,"/api/user",new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, ["Authorization",client.auth.get_auth_token.call(null)], null),new cljs.core.Keyword(null,"handler","handler",-195596612),(function (resp){
return cljs.core.reset_BANG_.call(null,client.dash.userdata,cljs.reader.read_string.call(null,new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(resp)));
}));
});
client.dash.get_table = (function client$dash$get_table(tableid){
return ajax.core.GET.call(null,["/api/tables/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(tableid)].join(''),new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, ["Authorization",client.auth.get_auth_token.call(null)], null),new cljs.core.Keyword(null,"handler","handler",-195596612),(function (resp){
return cljs.core.reset_BANG_.call(null,client.dash.table,cljs.reader.read_string.call(null,new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(resp)));
}));
});
client.dash.new_table = (function client$dash$new_table(){
return console.log("new-table");
});
client.dash.show_settings = (function client$dash$show_settings(){
return console.log("show-settings");
});
client.dash.side_bar = (function client$dash$side_bar(){
var tables = cljs.core.deref.call(null,client.dash.userdata).call(null,new cljs.core.Keyword(null,"tables","tables",1334623052));
var username = cljs.core.deref.call(null,client.dash.userdata).call(null,new cljs.core.Keyword(null,"username","username",1605666410));
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),"side-bar"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),"username"], null),username], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),"logout-sett-div"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"/logout",new cljs.core.Keyword(null,"title","title",636505583),"Logout"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),"logout-btn",new cljs.core.Keyword(null,"class","class",-2030961996),"logout-sett"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),"/images/logout32.png"], null)], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),client.dash.show_settings.call(null),new cljs.core.Keyword(null,"title","title",636505583),"Settings"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),"sett-btn",new cljs.core.Keyword(null,"class","class",-2030961996),"logout-sett"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),"/images/settings32.png"], null)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),"table-list"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),client.dash.new_table.call(null),new cljs.core.Keyword(null,"class","class",-2030961996),"tbl-list-link",new cljs.core.Keyword(null,"title","title",636505583),"New Table"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"tbl-list-div",new cljs.core.Keyword(null,"id","id",-1388402092),"new-tbl-div"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),"/images/new-table32.png",new cljs.core.Keyword(null,"id","id",-1388402092),"new-tbl-img"], null)], null)], null)], null)], null)], null);
});
client.dash.contents = (function client$dash$contents(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"contents"], null);
});
/**
 * App
 */
client.dash.app = (function client$dash$app(){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),"app"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [client.dash.side_bar], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [client.dash.contents], null)], null);
});
client.dash.load_init = (function client$dash$load_init(){

client.dash.get_user_data.call(null);

return reagent.core.render.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [client.dash.app], null),document.body);
});
client.dash.load_init.call(null);

//# sourceMappingURL=dash.js.map
