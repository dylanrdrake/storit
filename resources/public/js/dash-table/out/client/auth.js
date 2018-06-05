// Compiled by ClojureScript 1.10.238 {}
goog.provide('client.auth');
goog.require('cljs.core');
goog.require('clojure.string');
client.auth.get_auth_token = (function client$auth$get_auth_token(){
var cookie = decodeURIComponent(document.cookie);
if(clojure.string.includes_QMARK_.call(null,cookie,"authtoken")){
return cljs.core.subs.call(null,cookie,cljs.core.count.call(null,"authtoken="));
} else {
return null;
}
});

//# sourceMappingURL=auth.js.map
