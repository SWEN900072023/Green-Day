import "./signup.scss";
import Form from "../form-input/form-input";
import Button from "../button/button";
import { useState } from "react";
const Signup = () => {
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");
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
          name="FirstName"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        ></Form>
        <Form
          label="LastName"
          type="text"
          required
          onChange={(e) => setLastName(e.target.value)}
          name="LastName"
          value={lastName}
        ></Form>
        <Form
          label="Email"
          type="email"
          required
          onChange={(e) => setEmail(e.target.value)}
          name="email"
          value={email}
        ></Form>
        <Form
          label="Password"
          type="password"
          required
          onChange={(e) => setPassword(e.target.value)}
          name="password"
          value={password}
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
