import React from 'react';
import Aux from "../../../../../../../../aux";

//const images = require.context('../../../../../../../../assets/images/user', true);

const messages = (props) => {
    let image = '';


    let msgClass = ['media-body'];
    if(props.message.type) {
        msgClass = [...msgClass, 'chat-menu-content'];
    } else {
        msgClass = [...msgClass, 'chat-menu-reply'];
    }

    return (
        <Aux>
            <div className="media chat-messages">
                {image}
                <div className={msgClass.join(' ')}>
                    <div className="">
                        <p className="chat-cont">{props.message.msg}</p>
                    </div>
                    <p className="chat-time">{props.message.time}</p>
                </div>
            </div>
        </Aux>
    );
};

export default messages;