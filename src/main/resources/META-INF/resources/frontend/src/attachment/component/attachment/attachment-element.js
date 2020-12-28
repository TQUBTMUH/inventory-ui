import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class AttachmentElement extends PolymerElement {
    static get template() {
        return html`
        <div>
            <a href="/api/v1/attachment/[[attachment.id]]"
               target="_blank">[[attachment.name]]</a>
        </div>
`;
    }

    static get is() {
        return 'attachment-element';
    }
}

customElements.define(AttachmentElement.is, AttachmentElement);
