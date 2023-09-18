import React, { Component, useEffect } from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import "./eventBooking.css";
import { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Axiosapi from "../axiosAPI/api";
import { useSelector } from "react-redux";
import { selectorCurrentUser } from "../store/user/user.selector";
const EventBooking = () => {
  const navigate = useNavigate();
  const [counts, setCounts] = useState([{ id: 0, count: 0 }]);
  const [sections, setSections] = useState(null);
  const currentUser = useSelector(selectorCurrentUser);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const eventId = searchParams.get("eventId");
  useEffect(() => {
    async function getEventDetails() {
      await Axiosapi.get(`/public/events/${eventId}`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }).then((res) => {
        // console.log(res);
        setSections(res.data.data.sections);
      });
    }

    getEventDetails();
  }, []);

  useEffect(() => {
    if (sections !== null) {
      const newCounts = sections.map((row) => ({
        sectionId: row.id,
        quantity: 0,
        unitPrice: row.unitPrice,
        currency: row.currency,
      }));
      setCounts(newCounts);
    }
  }, [sections]);
  // console.log(counts);
  const increment = (id) => {
    setCounts((prevCounts) =>
      prevCounts.map((counter) =>
        counter.sectionId === id
          ? { ...counter, quantity: counter.quantity + 1 }
          : counter
      )
    );
  };
  // console.log(counts);
  const decrement = (id) => {
    setCounts((prevCounts) =>
      prevCounts.map((counter) =>
        counter.sectionId === id && counter.quantity > 0
          ? { ...counter, quantity: counter.quantity - 1 }
          : counter
      )
    );
  };
  const placeOrder = async () => {
    const order = counts.filter((counter) => counter.quantity !== 0);
    // console.log(order);
    await Axiosapi.post(
      "/customer/orders",
      {
        eventId,
        subOrders: order,
      },
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      }
    ).then((res) => {
      // console.log(res);
      alert("transaction success!");
      navigate("/home");
    });
  };
  return (
    <>
      <h1>it is booking system</h1>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ fontWeight: "bold" }}>Section Name</TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Currency
              </TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Ticket Capacity
              </TableCell>
              <TableCell align="center" sx={{ fontWeight: "bold" }}>
                Ticket Remaining
              </TableCell>
              {/* <TableCell align="center">Price</TableCell> */}
            </TableRow>
          </TableHead>
          <TableBody>
            {sections == null ? (
              <></>
            ) : (
              sections.map((row) => (
                <TableRow
                  key={row.id}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {row.name}
                    <br />
                    {row.unitPrice}$ for each
                    {/* <div class="counter">
                    <button onclick={() => decrement(index)}>-</button>
                    <span>{counts[index].counter}</span>
                    <button onclick={() => increment(index)}>+</button>
                  </div> */}
                    {counts.length !== sections.length ? (
                      <></>
                    ) : (
                      <div class="counter">
                        <button onClick={() => decrement(row.id)}>-</button>
                        <span>
                          {
                            counts.filter(
                              (counter) => counter.sectionId === row.id
                            )[0].quantity
                          }
                          {/* {index} */}
                        </span>
                        <button onClick={() => increment(row.id)}>+</button>
                      </div>
                    )}
                  </TableCell>
                  <TableCell align="center">{row.currency}</TableCell>
                  <TableCell align="center">{row.capacity}</TableCell>
                  <TableCell align="center">{row.remainingTickets}</TableCell>
                  {/* <TableCell align="center">{row.price}</TableCell> */}
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <div className="casher">
        <h3>
          Total fee:{" "}
          {counts.reduce(
            (accumulator, counter) =>
              accumulator + counter.quantity * counter.unitPrice,
            0
          )}
          $
        </h3>
        <button onClick={placeOrder}>Buy</button>
      </div>
    </>
  );
};

export default EventBooking;
