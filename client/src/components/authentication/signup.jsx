import "./signup.scss";
import Form from "../form-input/form-input";
import Button from "../button/button";
const Signup = () => {
  return (
    // <div className="container">
    <div className="sign-up-container">
      <h1>Don't have any account?</h1>
      <h2>Sign up with your account!</h2>
      <form onSubmit={console.log("click to sign up")}>
        <Form
          label="FirstName"
          type="text"
          required
          //   onChange={handleChange}
          name="FirstName"
          value=""
        ></Form>
        <Form
          label="LastName"
          type="text"
          required
          //   onChange={handleChange}
          name="LastName"
          value=""
        ></Form>
        <Form
          label="Email"
          type="email"
          required
          //   onChange={handleChange}
          name="email"
          value=""
        ></Form>
        <Form
          label="Password"
          type="password"
          required
          //   onChange={handleChange}
          name="password"
          value=""
        ></Form>
        <Form
          label="ConfirmPassword"
          type="password"
          required
          //   onChange={handleChange}
          name="confirmPassword"
          value=""
        ></Form>
        <Button type="submit" buttonType="google">
          Sign up
        </Button>
      </form>
    </div>
    // </div>
  );
};

export default Signup;
