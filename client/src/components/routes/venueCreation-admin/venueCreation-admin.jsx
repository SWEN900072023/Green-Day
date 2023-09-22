import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useState } from "react";
import Axiosapi from "../../axiosAPI/api";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";
const VenueCreation = () => {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [address, setAddress] = useState("");
  const [capacity, setCapacity] = useState("");
  const currentUser = useSelector(selectorCurrentUser);
  const createVenue = async (e) => {
    e.preventDefault();
    await Axiosapi.post(
      "/admin/venues",
      {
        name,
        address,
        capacity: Number(capacity),
      },
      {
<<<<<<< HEAD
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
=======
          headers: {
              Authorization: `Bearer ${currentUser.token}`,
          }
>>>>>>> origin
      }
    ).then((res) => {
      console.log(res);
      alert("create venue successfully!");
      //   navigate("/home");
    });
  };
  //   console.log(Number(capacity));
  return (
    // <div className="container">
    <div className="sign-up-container">
      <h1>Wanna create new venue?</h1>
      <h2>Start it right now!</h2>
      <form onSubmit={createVenue}>
        <Form
          label="Venue Name"
          type="text"
          required
          name="Venue Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        ></Form>
        <Form
          label="Address"
          type="text"
          required
          onChange={(e) => setAddress(e.target.value)}
          name="Address"
          value={address}
        ></Form>
        <Form
          label="Capacity"
          type="number"
          required
          onChange={(e) => setCapacity(e.target.value)}
          name="Capacity"
          value={capacity}
        ></Form>
        <Button type="submit" buttonType="google">
          Create venue
        </Button>
      </form>
    </div>
    // </div>
  );
};

export default VenueCreation;
