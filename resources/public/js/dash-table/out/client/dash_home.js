// Compiled by ClojureScript 1.10.238 {}
goog.provide('client.dash_home');
goog.require('cljs.core');
goog.require('client.dash');
goog.require('client.auth');
goog.require('reagent.core');
goog.require('clojure.string');
client.dash_home.dash_home_div = (function client$dash_home$dash_home_div(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p","p",151049309),client.auth.get_auth_token.call(null)], null);
});
client.dash_home.main = (function client$dash_home$main(){
var el = document.getElementById("dash-home-div");
return reagent.core.render.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [client.dash_home.dash_home_div], null),el);
});
client.dash_home.main.call(null);
client.dash.setup.call(null);

//# sourceMappingURL=dash_home.js.map
