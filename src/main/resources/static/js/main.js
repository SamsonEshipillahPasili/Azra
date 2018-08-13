var btnHtml = "<span>&nbsp;&nbsp;&nbsp;</span><img src='img/btn_load.gif' /><span>&nbsp;&nbsp;&nbsp;</span>";
var btnHtml2 = "<span>&nbsp;&nbsp;&nbsp;</span><img src='/img/btn_load.gif' /><span>&nbsp;&nbsp;&nbsp;</span>";
// validate fields
function validateLogIn(e) {
    // get the target of the event 
    var userName = $("#username").val();
    var password = $("#password").val();
    if (userName && password) {
        var target = e.target;
        $(target).html(btnHtml);

        // validate user on the server
       /* setTimeout(function () {
            window.location = "/Azra/dashboard";
            $(target).html("Log In");
        }, 3000);*/
        logIn();
    } else {
        // notify user that all fields are required. 
        $("#logInErrorMsg").fadeIn();
    }
}

// log in the user
function logIn(){
    $("#rUsernameField").val($("#username").val());
    $("#rPasswordField").val($("#password").val());
    $("#rSubmitButton").click();
}

function validateRegistration(e) {
    var regUsername = $("#regUsername").val();
    var regPhoneNumber = $("#regPhoneNumber").val();
    var regPassword = $("#regPassword").val();
    var confirmPassword = $("#confirmPassword").val();

    if (regUsername && regPhoneNumber && regPassword && confirmPassword) {
        if (regUsername === "sam@gmail.com") {
            $("#regErrorMsg")
                    .text("Username already exists!").fadeIn();
        } else if (regPassword !== confirmPassword) {
            $("#regErrorMsg")
                    .text("The passwords do not match!").fadeIn();
        } else {
            swal("Success", "Registration was successful. " +
                    "Kindly wait for approval from the administrator", "success");
        }
    } else {
        // notify user that all fields are required. 
        $("#regErrorMsg").fadeIn();
    }
}

// show the contribute dialog box
function showContributeDialog(currentPerson) {
    swal({
        title: "Your Contribution",
        text: "Please note that you can only contribute once!",
        type: "input",
        showCancelButton: true,
        closeOnConfirm: false,
        inputPlaceholder: "Enter amount"
    }, function (inputValue) {
        if (inputValue === false)
            return false;
        if (inputValue === "") {
            alert("Enter the amount!");
            return false;
        }
        var url = "/Azra/contribute/" + inputValue;
        $.post(url, function(response){
            if(response === "Ok"){
               swal("Great!", "Your contribution was saved!", "success");
            }else{
                swal("Uh oh!", response, "error");
            }
        });
    });
}

// add a new user to the system
function addUser(event){
    //
    var name = $("#addNameField").val();
    var phoneNumber = $("#addPhoneField").val();
    var gender = $("#addGenderField").val();

    if(name && phoneNumber && gender){
        if(gender === "-- Gender --"){
            $("#addMemberMsg").text("Select the gender, please").fadeIn();
        }else if(phoneNumber.length < 9){
            $("#addMemberMsg").text("Phone number must be at least 9 characters!").fadeIn();
        }else if(phoneNumber < 0){
            $("#addMemberMsg").text("Phone number cannot be negative!").fadeIn();
        }else{

            // submit the values to  REST endpoint to add the values
            var serverURL = "/Azra/addMember/" + name + "/" + phoneNumber + "/" + gender + "/";
            //var form = $("#addForm");
            //form.submit();
            $("#submitAddUserForm").click();
            //$(event.target).html(btnHtml2);
        }
    }else{
        $("#addMemberMsg").fadeIn();
    }
}

// delete a user
function deleteUser(event){
     var id = $(event.target).attr("data-username");

    swal({
      title: "Are you sure?",
      text: "The user with the id: " + id + " will be deleted permanently",
      type: "warning",
      showCancelButton: true,
      confirmButtonClass: "btn-danger",
      confirmButtonText: "Ok",
      closeOnConfirm: false
    },
    function(){
        id = "#" + id;
        $(id).click();
    });
}

// block a user
function blockUser(event){
     var id = $(event.target).attr("data-username");
    swal({
      title: "Are you sure?",
      text: "The user with the id: " + id + " will be blocked.",
      type: "warning",
      showCancelButton: true,
      confirmButtonClass: "btn-danger",
      confirmButtonText: "Block",
      closeOnConfirm: false
    },
    function(){
        id = "#" + id;
        $(id).click();
    });
}

// unblock a user
function unBlockUser(event){
    var id = $(event.target).attr("data-username");
    swal({
      title: "Are you sure?",
      text: "The user with the id: " + id + " will be unblocked.",
      type: "warning",
      showCancelButton: true,
      confirmButtonClass: "btn-success",
      confirmButtonText: "Unblock",
      closeOnConfirm: false
    },
    function(){
        id = "#" + id;
        $(id).click();
    });
}

// update the username
function updateUsername(){
      if($("#newUsernameUp").val() && $("#oldUsernameUp").val()){
           swal({
                  title: "Update your username",
                  text: "Once updated, you will not be able to update your username again.",
                  type: "warning",
                  showCancelButton: true,
                  confirmButtonClass: "btn-success",
                  confirmButtonText: "Okay",
                  closeOnConfirm: false
                },
                function(){
                    $("#updateUsernameBtn").click();
                });
      }else{
          $("#updateUsernameMsg").fadeIn();
      }
}


// update the password
function updatePassword(){
    var oldPassword = $("#oldPasswordUp").val();
    var newPassword = $("#newPasswordUp").val();
    var newPasswordConfirm = $("#confirmPasswordUp").val();

    if(oldPassword && newPassword && newPasswordConfirm){
        if(newPassword !== newPasswordConfirm){
            $("#passwordUpdateMsg").text("The passwords do not match!");
            $("#passwordUpdateMsg").fadeIn();
        }else{
            // continue with password update
            //$("#passwordForm").submit();
            $("#updatePasswordBtn").click();
        }
    }else{
        $("#passwordUpdateMsg").fadeIn();
    }
}

// update the username
function updatePhoneNumber(){

    // clear any other pre-existing error message.
    var phoneNumber = $("#phoneNumberUp").val();
    var name = $("#nameUp").val();
    if(phoneNumber && name){
        if(phoneNumber < 0){
            $("#phoneNumberMsg").text("Phone number cannot be negative").fadeIn()
        }else{
            //$("#phoneNumberForm").submit();
            $("#updateOtherBtn").click();
        }
    }else{
        $("#phoneNumberMsg").text("All fields are required.").fadeIn();
    }
}

// update the profile picture
function updateProfilePicture(){
    $("#profilePic").click();
}


// keep track of changes in value of the profile pic
function trackPhotoChanges(event){
    var target = event.target;
    $("#profileImage").attr("src", URL.createObjectURL(target.files[0]));
    $("#updateProfilePicForm").submit();
}

/*// open a payment cycle
function openPaymentCycle(){
     // "/Azra/newCycle/{amount}/{days}"

     swal({
            title: "New Payment Cycle",
            text: "Specify the amount per person",
            type: "input",
            showCancelButton: true,
            closeOnConfirm: false,
            inputPlaceholder: "Enter the amount."
        }, function (inputValue) {
            if(inputValue){
                var url = "/Azra/newCycle/" + inputValue;
                $.post(url, function(response){
                    if(response === "Ok"){
                        swal("Great", "You have created a new payment cycle of Ksh." + inputValue, "success");
                        setTimeout(function(){
                            window.location = "/Azra/paymentsCycle";
                        }, 400);
                    }else{
                        swal("Uh oh", "Failed to create a new payment cycle at the moment", "error");
                    }
                });
            }else{
                  if(inputValue === false) return false;
                  if(inputValue === ""){
                    alert("Enter the amount!");
                  }
            }

        });
}*/


// open a payment cycle
function openPaymentCycle(){
     var amount = $("#amountPerPerson").val();
     var interval = $("#interval").val();

     if(amount && interval){
        if(amount < 1){
            $("#paymentCycleMsg").text("Invalid amount specified!").fadeIn();
        }else if(interval === "-- Select Payment Interval --"){
           $("#paymentCycleMsg").text("All fields are required!").fadeIn();
        }else{
           $("#paymentCycleMsg").fadeOut();
           var url =  "/Azra/newCycle/" + amount + "/" + interval;
           $.post(url, function(response){
               if(response === "Ok"){
                   swal("Great", "You have created a new payment cycle of Ksh." + amount, "success");
                   setTimeout(function(){
                                    window.location = "/Azra/paymentsCycle";
                             }, 400);
               }else{
                    swal("Uh oh", response, "error");
               }
           });
        }
     }else{
         $("#paymentCycleMsg").text("All fields are required!").fadeIn();
     }
}



// close payment cycle
function closePaymentCycle(){
    swal({
          title: "Are you sure?",
          text: "No further payments will be made to this cycle!",
          type: "warning",
          showCancelButton: true,
          confirmButtonClass: "btn-danger",
          confirmButtonText: "Close.",
          closeOnConfirm: false
        },
        function(){
            var url = "/Azra/closeCycle/";
            $.post(url, function(response){
            if(response === "Ok"){
                swal("Great", "Payment Cycle successfully closed", "success");
                setTimeout(function(){
                    window.location = "/Azra/paymentsCycle";
                }, 400);
            }else{
                swal("Error", "Failed to close payment cycle.", "error");
            }
            });
        });
}


