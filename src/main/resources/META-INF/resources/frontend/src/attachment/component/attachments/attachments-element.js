import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class AttachmentsElement extends PolymerElement {
    static get template() {
        return html`
        <div>
            <template is="dom-repeat" items="[[attachments]]">
                <attachment-element attachment="[[item]]"></attachment-element>
            </template>
        </div>
`;
    }

    static get is() {
        return 'attachments-element';
    }
}

customElements.define(AttachmentsElement.is, AttachmentsElement);
