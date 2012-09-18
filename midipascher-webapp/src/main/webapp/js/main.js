var geocoder;
var map;
var marker;

function initialize() {
    //	MAP
    var latlng = new google.maps.LatLng(41.659, -4.714);
    var options = {
        zoom:16,
        center:latlng,
        mapTypeId:google.maps.MapTypeId.PLAN
    };

    map = new google.maps.Map(document.getElementById("map_canvas"), options);

    //GEOCODER
    geocoder = new google.maps.Geocoder();

    marker = new google.maps.Marker({
        map:map,
        draggable:true
    });

}

$(document).ready(function () {

    initialize();

}
