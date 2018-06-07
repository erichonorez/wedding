$(function() {
   galleryIds.forEach(element => {
    $(element).justifiedGallery({
        'lastRow': 'justify'
    });
   });
   $('table').addClass("table")
});