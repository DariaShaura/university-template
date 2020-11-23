$(document).ready(function(){

     $('#login').blur(function() {
            var check = checkAvailability();
            if (check) {
                $('#login').setCustomValidity("I expect an e-mail, darling!");
              } else {
                $('#login').setCustomValidity("");
              };

     });
});

function checkAvailability() {
        var result;
        $.getJSON("registration/availability", { name: $('#login').val() }, {dataType: "json"}, function(availability) {
            console.log('avail =' + availability);
            result = true;
        });
        return result;
}