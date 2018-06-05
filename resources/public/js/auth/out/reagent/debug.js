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
var G__2229__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__2229 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__2230__i = 0, G__2230__a = new Array(arguments.length -  0);
while (G__2230__i < G__2230__a.length) {G__2230__a[G__2230__i] = arguments[G__2230__i + 0]; ++G__2230__i;}
  args = new cljs.core.IndexedSeq(G__2230__a,0,null);
} 
return G__2229__delegate.call(this,args);};
G__2229.cljs$lang$maxFixedArity = 0;
G__2229.cljs$lang$applyTo = (function (arglist__2231){
var args = cljs.core.seq(arglist__2231);
return G__2229__delegate(args);
});
G__2229.cljs$core$IFn$_invoke$arity$variadic = G__2229__delegate;
return G__2229;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__2232__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__2232 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__2233__i = 0, G__2233__a = new Array(arguments.length -  0);
while (G__2233__i < G__2233__a.length) {G__2233__a[G__2233__i] = arguments[G__2233__i + 0]; ++G__2233__i;}
  args = new cljs.core.IndexedSeq(G__2233__a,0,null);
} 
return G__2232__delegate.call(this,args);};
G__2232.cljs$lang$maxFixedArity = 0;
G__2232.cljs$lang$applyTo = (function (arglist__2234){
var args = cljs.core.seq(arglist__2234);
return G__2232__delegate(args);
});
G__2232.cljs$core$IFn$_invoke$arity$variadic = G__2232__delegate;
return G__2232;
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
