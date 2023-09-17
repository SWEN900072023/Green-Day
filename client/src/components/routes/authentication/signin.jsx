import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useState } from "react";
import Axiosapi from "../../axiosAPI/api";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setCurrentUser } from "../../store/user/user.action";
const Signin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const signin = async (e) => {
    e.preventDefault();
    const data = new URLSearchParams();
    data.append("email", email);
    data.append("password", password);

    const encodedData = data.toString();
    console.log(encodedData);
    await Axiosapi.post("/login", encodedData, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    }).then((res) => {
      console.log(res);
      alert(res.data.message);
      dispatch(setCurrentUser(res.data.data));
      navigate("/home");
    });
  };
  return (
    <div className="sign-up-container">
      {/* <h1>Don't have any account?</h1> */}
      <h2>Sign in with your account!</h2>
      <form onSubmit={signin}>
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
          Sign in
        </Button>
      </form>
    </div>
  );
};

export default Signin;
