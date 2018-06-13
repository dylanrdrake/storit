// Compiled by ClojureScript 1.10.238 {}
goog.provide('client.dash');
goog.require('cljs.core');
goog.require('reagent.core');
client.dash.userdata = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
client.dash.table = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
client.dash.container = document.getElementById("container");
client.dash.app = (function client$dash$app(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.deref.call(null,client.dash.userdata)], null);
});
client.dash.load_init = (function client$dash$load_init(){
var get_user = (function (){
return client.dash.GET.call(null,"/api/user",new cljs.core.Keyword(null,"handler","handler",-195596612),(function (resp){
return cljs.core.reset_BANG_.call(null,client.dash.userdata,client.dash.read_string.call(null,new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(resp)));
}));
});
get_user.call(null);

return ((function (get_user){
return (function (){
return reagent.core.render.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [client.dash.app], null),client.dash.container);
});
;})(get_user))
});
client.dash.load_init.call(null);

//# sourceMappingURL=dash.js.map
