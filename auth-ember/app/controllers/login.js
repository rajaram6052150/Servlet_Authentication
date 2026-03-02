import Ember from 'ember';

export default Ember.Controller.extend({

  isLogin: true,
  isForgotPassword : false,
  message: '',
  email: '',
  password: '',
  confirmPassword: '',
  error : '',

  actions: {

    goToLogin : function() {
      this.setProperties({
        isLogin: true,
        isForgotPassword: false,
        email: '',
        password: '',
        confirmPassword: '',
        message: '',
        error: ''
      });
    },

    goToSignup() {
      this.setProperties({
        isLogin: false,
        isForgotPassword: false,
        email: '',
        password: '',
        confirmPassword: '',
        message: '',
        error: ''
      });
    },

    login() {

      Ember.$.ajax({
        type: "POST",
        url: "http://localhost:8080/login",
        dataType: "json",

        data: {
          email: this.get('email'),
          password: this.get('password')
        },

        success: (response) => {
          console.log("Login successful:", response.token);
          this.set('message', 'Login successful!');
          this.set('error', '');
        },

        error: (xhr) => {
          console.log("Status Code:", xhr.status);
          console.log("Response:", xhr.responseText);
          this.set('message', '');
          this.set('error', xhr.responseText);
        }
      });
    },

    signup() {

      if (this.get('password') !== this.get('confirmPassword')) {
        this.set('error', 'Passwords do not match');
        return;
      }

      Ember.$.ajax({
        type: "POST",
        url: "http://localhost:8080/register",
        dataType: "json",

        data: {
          email: this.get('email'),
          password: this.get('password')
        },

        success: (response) => {
          console.log("Signup successful:", response.token);  
          this.set('error', '');
          this.set('message', 'Signup successful!');
        },

        error: (xhr) => {
          this.set('message', '');
          this.set('error', xhr.responseText);
        }
      });

    },

    forgotPassword() {
      this.setProperties({
        isLogin: false,
        isForgotPassword: true,
        email: '',
        password: '',
        confirmPassword: '',
        message: '',
        error: ''
      });
    },

    googleLogin() {
      window.location.href = "http://localhost:8080/google-login";
    },

    sendResetLink() {

      Ember.$.ajax({
        type : "POST",
        url : "http://localhost:8080/forget-password",
        
        data : {
          email : this.get('email')
        },

        success : (response) => {
          this.set('message', 'Reset link sent to your email!');
          this.set('error', '');
          this.set('email', '');
        },
        error : (xhr) =>{
          this.set('error', xhr.responseText);
          this.set('message', '');
          this.set('email', '');
        }
      });
    },
  }
});

