import { Fragment, useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useNavigate } from "react-router-dom";
import AxiosApi from "../../axiosAPI/api";
import "./../home/home.css";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";

const User = () => {
  const navigate = useNavigate();
  const currentUser = useSelector(selectorCurrentUser);
  const [users, setUsers] = useState([]);
  const [planners, setPlanners] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [password, setPassword] = useState("");
  useEffect(() => {
    async function getUsersInfo() {
      await AxiosApi.get(`/admin/users`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setUsers(res.data.data);
      });
    }
    async function getCustomerInfo() {
      await AxiosApi.get(`/admin/users?type=customer`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setCustomers(res.data.data);
      });
    }
    async function getPlannerInfo() {
      await AxiosApi.get(`/admin/users?type=eventplanner`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setPlanners(res.data.data);
      });
    }
    getUsersInfo();
    getPlannerInfo();
    getCustomerInfo();
  }, []);
  // console.log(email);
  const createPlanner = async (e) => {
    e.preventDefault();
    await AxiosApi.post(
      `/register/event-planner`,
      {
        email,
        password,
        firstName,
        lastName,
      },
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }
    ).then((res) => {
      console.log(res);
      alert("create planner successfully");
    });
  };
  return (
    <>
      <h1>hi, user info page</h1>
      <div className="homeContainer">
        {" "}
        <div className="eventsList">
          {/* TODO: For event planner, it should display the event that they hosted */}
          <h1>All user info</h1>
          <List
            sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
          >
            {users.length === 0 ? (
              <></>
            ) : (
              users.map((user) => {
                return (
                  <>
                    <ListItem
                      className="listItem"
                      alignItems="flex-start"
                      // onClick={() => {
                      //   // TODO: Soft coded event name
                      //   navigate("/Summer BBQ/booking");
                      // }}
                    >
                      <ListItemAvatar></ListItemAvatar>
                      <ListItemText
                        primary={`${user.firstName} ${user.lastName}`}
                        secondary={
                          <Fragment>
                            <Typography
                              sx={{ display: "inline" }}
                              component="span"
                              variant="body2"
                              color="text.primary"
                            >
                              Email: {user.email}
                            </Typography>
                          </Fragment>
                        }
                      />
                    </ListItem>
                    {/* <button>cancel booking</button> */}
                    <Divider variant="inset" component="li" />
                  </>
                );
              })
            )}
          </List>
          <h1>All planner info</h1>
          <List
            sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
          >
            {planners.length === 0 ? (
              <></>
            ) : (
              planners.map((planner) => {
                return (
                  <>
                    <ListItem
                      className="listItem"
                      alignItems="flex-start"
                      // onClick={() => {
                      //   navigate(`planner/info`);
                      // }}
                    >
                      <ListItemAvatar></ListItemAvatar>
                      <ListItemText
                        primary={`${planner.firstName} ${planner.lastName}`}
                        secondary={
                          <Fragment>
                            <Typography
                              sx={{ display: "inline" }}
                              component="span"
                              variant="body2"
                              color="text.primary"
                            >
                              Email: {planner.email}
                            </Typography>
                          </Fragment>
                        }
                      />
                    </ListItem>
                    {/* <button>cancel booking</button> */}
                    <Divider variant="inset" component="li" />
                  </>
                );
              })
            )}
          </List>
          <h1>All customer info</h1>
          <List
            sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
          >
            {customers.length === 0 ? (
              <></>
            ) : (
              customers.map((customer) => {
                return (
                  <>
                    <ListItem
                      className="listItem"
                      alignItems="flex-start"
                      // onClick={() => {
                      //   navigate(`customer/info`);
                      // }}
                    >
                      <ListItemAvatar></ListItemAvatar>
                      <ListItemText
                        primary={`${customer.firstName} ${customer.lastName}`}
                        secondary={
                          <Fragment>
                            <Typography
                              sx={{ display: "inline" }}
                              component="span"
                              variant="body2"
                              color="text.primary"
                            >
                              Email: {customer.email}
                            </Typography>
                          </Fragment>
                        }
                      />
                    </ListItem>
                    {/* <button>cancel booking</button> */}
                    <Divider variant="inset" component="li" />
                  </>
                );
              })
            )}
          </List>
        </div>
        <div className="searchBar-calendar">
          <div className="event-creation-container">
            <div className="creation-container">
              <form onSubmit={createPlanner}>
                <Form
                  label="Email"
                  type="email"
                  required
                  name="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                ></Form>
                <Form
                  label="Password"
                  type="password"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                  name="Password"
                  value={password}
                ></Form>
                <Form
                  label="First Name"
                  type="text"
                  required
                  onChange={(e) => setFirstName(e.target.value)}
                  name="First Name"
                  value={firstName}
                ></Form>
                <Form
                  label="Last Name"
                  type="text"
                  required
                  onChange={(e) => setLastName(e.target.value)}
                  name="Last Name"
                  value={lastName}
                ></Form>
                <Button type="submit" buttonType="google">
                  Create Planner
                </Button>
              </form>
            </div>
            {/* <div className="section-creation-container"></div> */}
          </div>

          {/* <Button>Cancel event</Button> */}
        </div>
      </div>
    </>
  );
};
export default User;
