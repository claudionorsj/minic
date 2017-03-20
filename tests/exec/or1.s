	.text
	.globl main
main:
	movq $1, %rdi
	cmpq $0, %rdi
	je L34
L31:
	movq $1, %rdi
L30:
	addq $65, %rdi
	call putchar
	movq $0, %rdi
	cmpq $0, %rdi
	je L26
L23:
	movq $1, %rdi
L22:
	addq $65, %rdi
	call putchar
	movq $1, %rdi
	cmpq $0, %rdi
	je L18
L15:
	movq $1, %rdi
L14:
	addq $65, %rdi
	call putchar
	movq $0, %rdi
	cmpq $0, %rdi
	je L10
L7:
	movq $1, %rdi
L6:
	addq $65, %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L10:
	movq $0, %rdi
	cmpq $0, %rdi
	jne L7
	movq $0, %rdi
	jmp L6
	jmp L7
L18:
	movq $0, %rdi
	cmpq $0, %rdi
	jne L15
	movq $0, %rdi
	jmp L14
	jmp L15
L26:
	movq $2, %rdi
	cmpq $0, %rdi
	jne L23
	movq $0, %rdi
	jmp L22
	jmp L23
L34:
	movq $1, %rdi
	cmpq $0, %rdi
	jne L31
	movq $0, %rdi
	jmp L30
	jmp L31
	.data
