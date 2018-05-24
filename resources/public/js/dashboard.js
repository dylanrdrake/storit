function getAuthToken() {
  var name = "authtoken=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
}


function showMobileSideNav() {
  document.getElementById("hamburger").style.display = "none";
  document.getElementById("contents").style.display = "none";
  document.getElementById("tbl-ctrl-bar").style.display = "none";
  document.getElementById("side-bar").style.display = "block";
}
