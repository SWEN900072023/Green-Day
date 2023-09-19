import { Fragment, useEffect, useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import AxiosApi from "../axiosAPI/api";
import "./../routes/home/home.css";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../store/user/user.selector";

const CustomerPage = () => {
  const navigate = useNavigate();
  const currentUser = useSelector(selectorCurrentUser);
  const [bookings, setBookings] = useState([]);
  useEffect(() => {
    async function getBookings() {
      await AxiosApi.get(`/customer/orders`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        console.log(res);
        setBookings(res.data.data);
        // setReviews(res.data.data.reviews);
      });
    }
    getBookings();
  }, []);
  const exampleEvents = [
    { label: "Brunch this weekend?", year: 1994 },
    { label: "Summer BBQ", year: 1972 },
    { label: "Oui Oui", year: 1974 },
  ];
  return (
    <>
      {" "}
      <h1>hi, customer info</h1>
      <div className="eventsList">
        {/* TODO: For event planner, it should display the event that they hosted */}
        <h1>All booking for a customer</h1>
        <List
          sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        >
          {bookings.length === 0 ? (
            <></>
          ) : (
            bookings.map((booking) => {
              return (
                <>
                  <ListItem
                    className="listItem"
                    alignItems="flex-start"
                    // onClick={() => {
                    //   console.log(`hi im ${event.label}`);
                    //   // TODO: Soft coded event name
                    //   navigate("/Summer BBQ/booking");
                    // }}
                  >
                    <ListItemAvatar>
                      <Avatar
                        alt="Remy Sharp"
                        src="/static/images/avatar/1.jpg"
                      />
                    </ListItemAvatar>
                    <ListItemText
                      primary={`${booking}`}
                      secondary={
                        <Fragment>
                          <Typography
                            sx={{ display: "inline" }}
                            component="span"
                            variant="body2"
                            color="text.primary"
                          >
                            Ali Connors
                          </Typography>
                          {
                            " — I'll be in your neighborhood doing errands this…"
                          }
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
    </>
  );
};
export default CustomerPage;
