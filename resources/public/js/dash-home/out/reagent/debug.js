// Compiled by ClojureScript 1.10.238 {}
goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = typeof console !== 'undefined';
reagent.debug.tracking = false;
if(typeof reagent.debug.warnings !== 'undefined'){
} else {
reagent.debug.warnings = cljs.core.atom.call(null,null);
}
if(typeof reagent.debug.track_console !== 'undefined'){
} else {
reagent.debug.track_console = (function (){var o = ({});
o.warn = ((function (o){
return (function() { 
var G__1315__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__1315 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__1316__i = 0, G__1316__a = new Array(arguments.length -  0);
while (G__1316__i < G__1316__a.length) {G__1316__a[G__1316__i] = arguments[G__1316__i + 0]; ++G__1316__i;}
  args = new cljs.core.IndexedSeq(G__1316__a,0,null);
} 
return G__1315__delegate.call(this,args);};
G__1315.cljs$lang$maxFixedArity = 0;
G__1315.cljs$lang$applyTo = (function (arglist__1317){
var args = cljs.core.seq(arglist__1317);
return G__1315__delegate(args);
});
G__1315.cljs$core$IFn$_invoke$arity$variadic = G__1315__delegate;
return G__1315;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__1318__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__1318 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__1319__i = 0, G__1319__a = new Array(arguments.length -  0);
while (G__1319__i < G__1319__a.length) {G__1319__a[G__1319__i] = arguments[G__1319__i + 0]; ++G__1319__i;}
  args = new cljs.core.IndexedSeq(G__1319__a,0,null);
} 
return G__1318__delegate.call(this,args);};
G__1318.cljs$lang$maxFixedArity = 0;
G__1318.cljs$lang$applyTo = (function (arglist__1320){
var args = cljs.core.seq(arglist__1320);
return G__1318__delegate(args);
});
G__1318.cljs$core$IFn$_invoke$arity$variadic = G__1318__delegate;
return G__1318;
})()
;})(o))
;

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
reagent.debug.tracking = true;

cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

f.call(null);

var warns = cljs.core.deref.call(null,reagent.debug.warnings);
cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

reagent.debug.tracking = false;

return warns;
});

//# sourceMappingURL=debug.js.map
