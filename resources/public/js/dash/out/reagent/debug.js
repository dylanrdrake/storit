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
var G__810__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__810 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__811__i = 0, G__811__a = new Array(arguments.length -  0);
while (G__811__i < G__811__a.length) {G__811__a[G__811__i] = arguments[G__811__i + 0]; ++G__811__i;}
  args = new cljs.core.IndexedSeq(G__811__a,0,null);
} 
return G__810__delegate.call(this,args);};
G__810.cljs$lang$maxFixedArity = 0;
G__810.cljs$lang$applyTo = (function (arglist__812){
var args = cljs.core.seq(arglist__812);
return G__810__delegate(args);
});
G__810.cljs$core$IFn$_invoke$arity$variadic = G__810__delegate;
return G__810;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__813__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__813 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__814__i = 0, G__814__a = new Array(arguments.length -  0);
while (G__814__i < G__814__a.length) {G__814__a[G__814__i] = arguments[G__814__i + 0]; ++G__814__i;}
  args = new cljs.core.IndexedSeq(G__814__a,0,null);
} 
return G__813__delegate.call(this,args);};
G__813.cljs$lang$maxFixedArity = 0;
G__813.cljs$lang$applyTo = (function (arglist__815){
var args = cljs.core.seq(arglist__815);
return G__813__delegate(args);
});
G__813.cljs$core$IFn$_invoke$arity$variadic = G__813__delegate;
return G__813;
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
