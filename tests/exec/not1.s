	.text
	.globl main
main:
	movq $0, %rdi
	cmpq $0, %rdi
	je L13
	movq $0, %rdi
L12:
	addq $65, %rdi
	call putchar
	movq $1, %rdi
	cmpq $0, %rdi
	je L7
	movq $0, %rdi
L6:
	addq $65, %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L7:
	movq $1, %rdi
	jmp L6
L13:
	movq $1, %rdi
	jmp L12
	.data
